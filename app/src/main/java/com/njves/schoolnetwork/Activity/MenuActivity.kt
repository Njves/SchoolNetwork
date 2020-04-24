package com.njves.schoolnetwork.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.njves.schoolnetwork.Models.KeyboardUtils
import com.njves.schoolnetwork.Models.NetworkService

import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.request.ProfileService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.callback.OnLogoutListener
import com.njves.schoolnetwork.fragments.ProfileFragment
import com.njves.schoolnetwork.presenter.menu.IMenu
import com.njves.schoolnetwork.presenter.menu.MenuPresenter
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity(), ProfileFragment.OnProfileUpdateListener, IMenu {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView : NavigationView
    private lateinit var toolbar : Toolbar
    private var presenter = MenuPresenter(this)
    companion object{
        const val TAG = "MenuActivity"
    }
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        val storage = AuthStorage(this)
        presenter.getProfile(storage.getUserDetails()!!)

        fab.setOnClickListener{
            navController.navigate(R.id.nav_task_edit)
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val appBarLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
            appBarLayout.setExpanded(true, true)
            KeyboardUtils.hideKeyboard(this)
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
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        KeyboardUtils.hideKeyboard(this)
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



    private fun inflateHeaderView(header : View, data : Profile?) {
        val avatar = header.findViewById<ImageView>(R.id.ivAva)
        val tvName = header.findViewById<TextView>(R.id.tvName)
        val tvPos = header.findViewById<TextView>(R.id.tvPos)
        val namePlaceholder = resources.getString(R.string.header_name_placeholder, data?.firstName, data?.lastName)
        tvName.text = namePlaceholder
        val posTitle = data?.positionTitle
        var `class` = data?.classValue
        if(`class`=="0"){
            `class` = ""
        }
        tvPos.text = resources.getString(R.string.position_placeholder, posTitle, `class`)


        Picasso.get().load(R.drawable.ic_avatar_placeholder_black_24dp).placeholder(R.drawable.ic_avatar_placeholder_black_24dp).into(avatar)
    }




    override fun onUpdateProfile() {
        val storage = AuthStorage(this)
        presenter.getProfile(storage.getUserDetails()!!)

    }


    override fun onSuccess(profile: Profile) {
        val header = navView.getHeaderView(0)
        inflateHeaderView(header, profile)
        AuthStorage.getInstance(this).setLocalUserProfile(profile)
    }

    override fun onError(message: String) {
        Toast.makeText(this@MenuActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Toast.makeText(this@MenuActivity, "Не удалось получить профиль", Toast.LENGTH_SHORT).show()
        Log.wtf(TAG,t.toString())
    }

    override fun showProgressBar() {
        TODO("Not yet implemented")
    }

    override fun hideProgressBar() {
        TODO("Not yet implemented")
    }
}


