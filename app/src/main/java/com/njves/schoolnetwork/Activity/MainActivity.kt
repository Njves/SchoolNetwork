package com.njves.schoolnetwork.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.callback.UpdateToolbarTitleListener

import com.njves.schoolnetwork.callback.OnAuthPassedListener
import com.njves.schoolnetwork.Fragments.AuthFragment


class MainActivity : AppCompatActivity(),OnAuthPassedListener, UpdateToolbarTitleListener {
    var currentFragment : Fragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportFragmentManager.addOnBackStackChangedListener {
            val isBack = supportFragmentManager.backStackEntryCount>0
            if(supportFragmentManager.backStackEntryCount>0)
            {
                Log.d("MainActivityBackStack", supportFragmentManager.backStackEntryCount.toString()+", $isBack")
                supportActionBar?.setDisplayShowHomeEnabled(true)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
            else{
                supportActionBar?.setDisplayShowHomeEnabled(false)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }

        }

        // TODO: Добавить дополнительную проверку на сервере
        // Проверяем если пользователь уже авторизован на телефоне

        if(checkUser()) {
            startMenuActivity()
        }
        else {
            if(currentFragment==null)
            {
                // Показываем фрагмент авторизации
                currentFragment = AuthFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.authContainer, currentFragment as AuthFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onSuccess(uid : String?) {
        val storage = AuthStorage(this)
        storage.setLogged(true)
        storage.setUserDetails(uid)
        startMenuActivity()
    }
    private fun checkUser() : Boolean
    {
        val storage = AuthStorage(this)
        Log.d("MainActivity", storage.getUserDetails()?:"")
        if(storage.isLogged()==true && storage.getUserDetails()!=null)
        {
            return true
        }
        return false
    }
    // TODO: Придумать нормальное название метода
    private fun startMenuActivity(){
        val intent = Intent(this,MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



    override fun updateActionBar(title: String) {
        supportActionBar?.setTitle(title)

    }


}