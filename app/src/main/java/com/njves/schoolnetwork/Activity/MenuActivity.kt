package com.njves.schoolnetwork.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.request.ProfileService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnLogoutListener
import com.njves.schoolnetwork.fragments.ProfileFragment
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity(), OnLogoutListener, ProfileFragment.OnProfileUpdateListener, Callback<NetworkResponse<Profile>> {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView : NavigationView
    private var actionDone : MenuItem? = null
    private var actionAbout : MenuItem? = null
    private lateinit var toolbar : Toolbar

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
       val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )
        val storage = AuthStorage(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        fab.setOnClickListener{
            navController.navigate(R.id.nav_task_edit)
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id)
            {
                R.id.nav_task_detail -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_clear_white_24dp, theme)
                }

            }
            if(destination.id==R.id.nav_task_tab){
                fab.visibility = View.VISIBLE
            }else{
                fab.visibility = View.GONE
            }
            //actionDone?.isVisible = destination.id==R.id.nav_task_edit || destination.id == R.id.nav_profile
        }
        val profileService = NetworkService.instance.getRetrofit().create(ProfileService::class.java)
        val getProfile = profileService.getProfile("GET", storage.getUserDetails()?:"0")
        getProfile.enqueue(this)



    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onLogout() {
        val storage = AuthStorage(this)
        storage.clearUserDetails()
        storage.setLogged(false)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun inflateHeaderView(header : View, data : Profile?) {
        val avatar = header.findViewById<ImageView>(R.id.ivAva)
        val tvName = header.findViewById<TextView>(R.id.tvName)
        val tvPos = header.findViewById<TextView>(R.id.tvPos)
        val namePlaceholder = resources.getString(R.string.header_name_placeholder, data?.firstName, data?.lastName)
        tvName.text = namePlaceholder
        val posTitle = data?.positionTitle
        val `class` = data?.classValue
        tvPos.text = resources.getString(R.string.position_placeholder, posTitle, `class`)


        Picasso.get().load(R.drawable.ic_avatar_placeholder_black_24dp).placeholder(R.drawable.ic_avatar_placeholder_black_24dp).into(avatar)
    }




    override fun onUpdateProfile() {
        val storage = AuthStorage(this)
        val profileService = NetworkService.instance.getRetrofit().create(ProfileService::class.java)
        val getProfile = profileService.getProfile("GET", storage.getUserDetails()?:"0")
        getProfile.enqueue(this)

    }

    override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
        Log.d("MenuActivity", "Failure request: $t")
        Toast.makeText(this@MenuActivity, "Произашла ошибка запроса к серверу", Toast.LENGTH_SHORT).show()
    }

    override fun onResponse(call: Call<NetworkResponse<Profile>>, response: Response<NetworkResponse<Profile>>) {
        val header = navView.getHeaderView(0)
        val code = response.body()?.code
        val message = response.body()?.message
        if(code==0) {
            val data = response.body()?.data
            Log.d("MenuActivity", data.toString())
            inflateHeaderView(header, data)
        }else{
            // TODO: Временное решение
            Toast.makeText(this@MenuActivity, message, Toast.LENGTH_SHORT).show()
        }
    }



}


