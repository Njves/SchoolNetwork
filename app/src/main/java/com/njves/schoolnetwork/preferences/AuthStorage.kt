package com.njves.schoolnetwork.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.njves.schoolnetwork.Models.network.models.profile.Profile

class AuthStorage(val context : Context?) {
    companion object{
        private val instance : AuthStorage? = null
        fun getInstance(context: Context?) : AuthStorage{
            return instance ?: AuthStorage(context)
        }
        const val TAG = "AuthStorage"
        const val AUTH_STORAGE_NAME = "authStorage"
        const val USER_UID = "uid"
        const val IS_LOGGED = "logged"
        const val USER_NAME = "name"
    }


    private var preferences : SharedPreferences? = null
    private var editor : SharedPreferences.Editor? = null
    init {
        preferences = context?.getSharedPreferences(AUTH_STORAGE_NAME, MODE_PRIVATE)
        editor = preferences?.edit()
    }
    public fun setUserDetails(uid : String?) {
        editor?.putString(USER_UID, uid)
        editor?.apply()
    }
    public fun getUserDetails() : String? {
        return preferences?.getString(USER_UID, null)
    }
    public fun isLogged() : Boolean? {
        return preferences?.getBoolean(IS_LOGGED, false)
    }
    public fun setLogged(isLogged : Boolean) {
        editor?.putBoolean(IS_LOGGED, isLogged)
        editor?.apply()
    }
    public fun clearUserDetails() {
        preferences?.edit()?.remove(USER_UID)?.apply()
    }
    public fun getUserName() : String? {
        return preferences?.getString(USER_NAME, "")
    }
    public fun setUserName(name : String) {
        editor?.putString(USER_NAME, name)?.apply()
    }


}