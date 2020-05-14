package com.njves.schoolnetwork.Activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.njves.schoolnetwork.Models.ConnectionChecker
import com.njves.schoolnetwork.Models.KeyboardUtils
import com.njves.schoolnetwork.Models.network.models.profile.Profile
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.fragments.ProfileFragment
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.preferences.ProfilePreferences
import com.njves.schoolnetwork.presenter.menu.IMenu
import com.njves.schoolnetwork.presenter.menu.MenuPresenter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MenuActivity : AppCompatActivity(), ProfileFragment.OnProfileUpdateListener, IMenu {
    private lateinit var ivAvatar: CircleImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvPosition: TextView
    private lateinit var pbMenu: ProgressBar
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView : NavigationView
    private lateinit var toolbar : Toolbar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var fabEditTask: FloatingActionButton
    private var presenter = MenuPresenter(this)
    companion object{
        const val TAG = "MenuActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        ivAvatar = headerView.findViewById(R.id.ivAvatar)
        tvFullName = headerView.findViewById(R.id.tvName)
        tvPosition = headerView.findViewById(R.id.tvPos)
        pbMenu = headerView.findViewById(R.id.pbMenu)
        appBarLayout = findViewById(R.id.appBarLayout)
        fabEditTask = findViewById(R.id.fabAdd)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            navController.graph, drawerLayout
        )
        fabEditTask.setOnClickListener{
            navController.navigate(R.id.nav_task_edit)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        val storage = AuthStorage(this)
        presenter.getProfile(storage.getUserDetails()!!)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            appBarLayout.setExpanded(true, true)
            KeyboardUtils.hideKeyboard(this)
            when(destination.id)
            {
                R.id.nav_task_detail -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_clear_white_24dp, theme)
                }
            }
            if(destination.id==R.id.nav_task_tab){
                fabEditTask.show()
            }else{
                fabEditTask.hide()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        KeyboardUtils.hideKeyboard(this)
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun inflateHeaderView(data : Profile?) {
        val namePlaceholder = resources.getString(R.string.header_name_placeholder, data?.firstName, data?.lastName)
        tvFullName.text = namePlaceholder
        val posTitle = data?.positionTitle
        var `class` = data?.classValue
        if(`class`=="0"){
            `class` = ""
        }
        tvPosition.text = resources.getString(R.string.position_placeholder, posTitle, `class`)
        presenter.setProfileAvatar(data!!.avatarLink)
    }

    override fun onUpdateProfile() {
        val storage = AuthStorage(this)
        presenter.getProfile(storage.getUserDetails()!!)
    }
    override fun onSuccess(profile: Profile?) {
        if(profile==null){
            tvFullName.text = "Создайте профиль"
            ivAvatar.visibility = View.GONE
            tvPosition.text = "Нажмите чтобы создать профиль"
            val header = navView.getHeaderView(0)
            header.setOnClickListener{
                findNavController(R.id.nav_host_fragment).navigate(R.id.nav_profile)
            }
            return
        }
        inflateHeaderView(profile)
        ProfilePreferences.getInstance(this).setLocalUserProfile(profile)
        ProfilePreferences.getInstance(this).setIsProfile(true)
    }

    override fun onError(message: String?) {

        Toast.makeText(this@MenuActivity, "Произашла ошибка $message", Toast.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Toast.makeText(this@MenuActivity, "Не удалось получить профиль", Toast.LENGTH_SHORT).show()
        Log.wtf(TAG,t.toString())
    }

    override fun showProgressBar() {
        pbMenu.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pbMenu.visibility = View.GONE
    }

    override fun onSetAvatarImage(avatarLink: String) {
        Picasso.get().load(avatarLink).noFade().into(ivAvatar)
    }

}


