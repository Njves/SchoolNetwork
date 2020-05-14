package com.njves.schoolnetwork.preferences

import android.content.Context
import android.content.SharedPreferences
import com.njves.schoolnetwork.Models.network.models.profile.Profile

class ProfilePreferences private constructor(context: Context?) {

    private var preferences: SharedPreferences?
    companion object{
        public const val PREF_PROFILE = "pref_profile"
        private var instance: ProfilePreferences? = null
        fun getInstance(context: Context?) : ProfilePreferences{
            return instance ?: ProfilePreferences(context)
        }

        const val PROFILE_UID = "uid"
        const val PROFILE_FIRST_NAME = "first_name"
        const val PROFILE_LAST_NAME = "last_name"
        const val PROFILE_MIDDLE_NAME = "last_name"
        const val PROFILE_POSITION = "position"
        const val PROFILE_CLASS = "class"
        const val PROFILE_AVATAR_LINK = "avatar_link"
        const val IS_PROFILE = "is_profile"
    }
    init{
        preferences = context!!.getSharedPreferences(PREF_PROFILE, Context.MODE_PRIVATE)
    }
    fun getLocalUserProfile() : Profile? {
        val editor = preferences!!.edit()
        val uid = preferences?.getString(PROFILE_UID, null)
        val firstName = preferences?.getString(PROFILE_FIRST_NAME, "")
        val lastName = preferences?.getString(PROFILE_LAST_NAME, "")
        val middleName = preferences?.getString(PROFILE_MIDDLE_NAME, "")
        val position = preferences?.getInt(PROFILE_POSITION, 0)
        val profileClass = preferences?.getInt(PROFILE_CLASS, 0)
        val avatarLink = preferences?.getString(PROFILE_AVATAR_LINK, "")
        return Profile(
            uid,
            firstName!!,
            lastName!!,
            middleName!!,
            position!!,
            null,
            profileClass ?: 0,
            null,
            avatarLink!!
        )
    }
    fun setLocalUserProfile(profile: Profile){
        val editor = preferences!!.edit()
        editor?.putString(PROFILE_FIRST_NAME,profile.firstName)
        editor?.putString(PROFILE_LAST_NAME,profile.lastName)
        editor?.putString(PROFILE_MIDDLE_NAME,profile.middleName)
        editor?.putInt(PROFILE_POSITION,profile.position)
        editor?.putInt(PROFILE_CLASS,profile.`class`)
        editor?.putString(PROFILE_AVATAR_LINK,profile.avatarLink)
        editor?.apply()
        setIsProfile(true)
    }
    fun getIsProfile() : Boolean{
        return preferences!!.getBoolean(IS_PROFILE, false)
    }
    fun setIsProfile(flag: Boolean){
        val editor = preferences!!.edit()
        editor!!.putBoolean(IS_PROFILE, flag)
        editor.apply()
    }
}