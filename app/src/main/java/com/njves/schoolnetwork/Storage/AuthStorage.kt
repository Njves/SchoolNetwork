package com.njves.schoolnetwork.Storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log

class AuthStorage(context : Context?) {


    val isLogged = "isLogged"
    val isFilledProfile = "isFilledProfile"

    companion object{
        const val TAG = "AuthStorage"
        const val AUTH_STORAGE_NAME = "authStorage"
        const val USER_UID = "uid"
        const val IS_LOGGED = "logged"
    }
    private var preferences : SharedPreferences? = null
    private var editor : SharedPreferences.Editor? = null
    init {
        preferences = context?.getSharedPreferences(AUTH_STORAGE_NAME, MODE_PRIVATE)
        editor = preferences?.edit()
    }
    public fun setUserDetails(uid : String?)
    {
        editor?.putString(USER_UID, uid)
        editor?.apply()
    }
    public fun getUserDetails() : String?
    {

        return preferences?.getString(USER_UID, null)
    }
    public fun isLogged() : Boolean?
    {
        return preferences?.getBoolean(IS_LOGGED, false)
    }
    public fun setLogged(isLogged : Boolean)
    {
        editor?.putBoolean(IS_LOGGED, isLogged)
        editor?.apply()
    }

}