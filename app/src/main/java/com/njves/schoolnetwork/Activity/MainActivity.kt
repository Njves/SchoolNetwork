package com.njves.schoolnetwork.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.njves.schoolnetwork.Fragments.AuthFragment
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage

import com.njves.schoolnetwork.callback.OnSuccessAuthListener

class MainActivity : AppCompatActivity(),OnSuccessAuthListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(false);
        // TODO: Добавить дополнительную проверку на сервере
        // Проверяем если пользователь уже авторизован на телефоне
        if(!checkUser()) {
            //enterInSystem()
        }
        else {
            // Показываем фрагмент авторизации
            val authFragment = AuthFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.authContainer, authFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onSuccess(uid : String?) {
        val storage = AuthStorage(this)
        storage.setUserDetails(uid)
        enterInSystem()
    }
    private fun checkUser() : Boolean
    {
        val storage = AuthStorage(this)
        if(storage.getUserDetails()!=null&&storage.getUserDetails()!="")
        {
            return true
        }
        return false
    }
    // TODO: Придумать нормальное название метода
    private fun enterInSystem(){
        val intent = Intent(this,MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }



}
