package com.njves.schoolnetwork.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.njves.schoolnetwork.Fragments.TaskEditFragment
import com.njves.schoolnetwork.Fragments.TaskFragment
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.profile.UserProfile
import com.njves.schoolnetwork.Models.network.request.ProfileService
import com.njves.schoolnetwork.Models.network.request.UserProfileService
import com.njves.schoolnetwork.Models.network.request.UserService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnLogoutListener
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity(), OnLogoutListener, TaskFragment.OnFragmentInteraction, TaskEditFragment.OnFragmentInteraction {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )
        val storage = AuthStorage(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val header = navView.getHeaderView(0)
        val profileService = NetworkService.instance.getRetrofit().create(ProfileService::class.java)
        val getProfile = profileService.getProfile("GET", storage.getUserDetails()?:"0")
        getProfile.enqueue(object : Callback<NetworkResponse<Profile>>{
            override fun onFailure(call: Call<NetworkResponse<Profile>>, t: Throwable) {
                Log.d("MenuActivity", "Failure request: $t")
                Toast.makeText(this@MenuActivity, "Произашла ошибка запроса к серверу", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<NetworkResponse<Profile>>,
                response: Response<NetworkResponse<Profile>>
            ) {
                val code = response.body()?.code
                val message = response.body()?.message
                if(code==0) {
                    val data = response.body()?.data
                    inflateHeaderView(header, data)
                }else{
                    // TODO: Временное решение
                    Toast.makeText(this@MenuActivity, message, Toast.LENGTH_SHORT).show()
                }
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
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



    override fun openEditMenu() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.action_nav_task_to_nav_task_edit)
    }

    override fun onClose() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigateUp()
    }
    private fun inflateHeaderView(header : View, data : Profile?) {
        val avatar = header.findViewById<ImageView>(R.id.ivAva)
        val tvName = header.findViewById<TextView>(R.id.tvName)
        val tvPos = header.findViewById<TextView>(R.id.tvPos)
        tvName.text = "${data?.firstName}" + ", ${data?.lastName}"
        val schoolClass = data?.`class`
        // TODO: Временное решение
        when(data?.position) {
            1-> tvPos.text = "Учитель, $schoolClass"
            2->tvPos.text = "Зауч"
        }

        Picasso.get().load(R.drawable.ic_avatar_placeholder_black_24dp).placeholder(R.drawable.ic_avatar_placeholder_black_24dp).into(avatar)
    }



}

