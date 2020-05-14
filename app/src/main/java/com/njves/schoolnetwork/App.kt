package com.njves.schoolnetwork

import android.app.Application
import android.content.Context
import android.widget.Toast
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.ZonedDateTime

class App : Application() {
    companion object{
        private lateinit var context: Context
        fun getAppContext() : Context {
            return context
        }
    }
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        context = applicationContext

    }


}