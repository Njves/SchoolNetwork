package com.njves.schoolnetwork.preferences

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.text.TextUtils

class StatusPreferences(val context: Context?) {
    private var preferences: SharedPreferences = context?.getSharedPreferences("statusPref", MODE_PRIVATE)!!

    companion object{
        private var instance: StatusPreferences? = null
        public fun getInstance(context: Context): StatusPreferences{
            if(instance==null){
                instance = StatusPreferences(context)
                return instance!!
            }
            return instance!!
        }
        public const val UID = "uid"
    }

    public fun setStatus(uid: String,status: Int){
        if(!TextUtils.isEmpty(uid)) {
            val editor = preferences.edit()
            editor.putInt(uid, status)
            editor.apply()
        }
    }
    public fun getStatus(uid: String): Int{
        return preferences.getInt(uid, 0)
    }

}