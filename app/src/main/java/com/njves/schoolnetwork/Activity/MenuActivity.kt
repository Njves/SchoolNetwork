package com.njves.schoolnetwork.Activity

import android.content.Intent
import android.os.Bundle
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
import android.widget.ImageView
import android.widget.TextView
import com.njves.schoolnetwork.Fragments.TaskEditFragment
import com.njves.schoolnetwork.Fragments.TaskFragment
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.request.UserService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.OnLogoutListener

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

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val userService = NetworkService.instance.getRetrofit().create(UserService::class.java)

        val header = navView.getHeaderView(0)
        val tvHeader = header.findViewById<TextView>(R.id.tvName)
        val ivAva = header.findViewById<ImageView>(R.id.ivAva)
        val tvPos = findViewById<TextView>(R.id.tvPos)
        val storage = AuthStorage(this)
        val callUserProfile =   userService.getUser(storage.getUserDetails()?:"")
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
}
