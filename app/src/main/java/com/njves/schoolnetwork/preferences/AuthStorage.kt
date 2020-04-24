package com.njves.schoolnetwork.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.njves.schoolnetwork.Models.network.models.auth.Profile

class AuthStorage(val context : Context?) {
    companion object{
        val instance : AuthStorage? = null
        fun getInstance(context: Context?) : AuthStorage{
            return instance ?: AuthStorage(context)
        }
        const val TAG = "AuthStorage"
        const val AUTH_STORAGE_NAME = "authStorage"
        const val USER_UID = "uid"
        const val IS_LOGGED = "logged"
        const val USER_NAME = "name"
        const val PROFILE_UID = "uid"
        const val PROFILE_FIRST_NAME = "first_name"
        const val PROFILE_LAST_NAME = "last_name"
        const val PROFILE_MIDDLE_NAME = "last_name"
        const val PROFILE_POSITION = "position"
        const val PROFILE_CLASS = "class"
        const val PROFILE_AVATAR_LINK = "avatar_link"

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
    public fun clearUserDetails()
    {
        preferences?.edit()?.remove(USER_UID)?.apply()
    }
    public fun getUserName() : String?
    {
        return preferences?.getString(USER_NAME, "")
    }
    public fun setUserName(name : String)
    {
        editor?.putString(USER_NAME, name)?.apply()
    }

    fun getLocalUserProfile() : Profile? {
        val uid = preferences?.getString(PROFILE_UID, null)
        val firstName = preferences?.getString(PROFILE_FIRST_NAME, null)
        val lastName = preferences?.getString(PROFILE_LAST_NAME, null)
        val middleName = preferences?.getString(PROFILE_MIDDLE_NAME, null)
        val position = preferences?.getInt(PROFILE_POSITION, 0)
        val profileClass = preferences?.getString(PROFILE_CLASS, null)
        val avatarLink = preferences?.getString(PROFILE_AVATAR_LINK, null)
        return Profile(uid, firstName!!, lastName!!, middleName!!, position!!,null, profileClass?:"0",null, avatarLink!!)
    }
    fun setLocalUserProfile(profile: Profile){
        editor?.putString(PROFILE_FIRST_NAME,profile.firstName)
        editor?.putString(PROFILE_LAST_NAME,profile.lastName)
        editor?.putString(PROFILE_MIDDLE_NAME,profile.middleName)
        editor?.putInt(PROFILE_POSITION,profile.position)
        editor?.putString(PROFILE_CLASS,profile.`class`)
        editor?.putString(PROFILE_AVATAR_LINK,profile.avatarLink)
        editor?.apply()
    }
}