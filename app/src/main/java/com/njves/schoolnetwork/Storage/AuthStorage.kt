package com.njves.schoolnetwork.Storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AuthStorage(context : Context?) {
    val AUTH_STORAGE_NAME = "authStorage"
    val isLogged = "isLogged"
    private var preferences : SharedPreferences? = null
    private var editor : SharedPreferences.Editor? = null
    init {
        preferences = context?.getSharedPreferences(AUTH_STORAGE_NAME, MODE_PRIVATE)
        editor = preferences?.edit()
    }
    public fun saveData()
    {

    }
    public fun getData() : Boolean?
    {
        return preferences?.getBoolean(isLogged, false)
    }
}