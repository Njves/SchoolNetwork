package com.njves.schoolnetwork

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.threeten.bp.ZonedDateTime

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }
}