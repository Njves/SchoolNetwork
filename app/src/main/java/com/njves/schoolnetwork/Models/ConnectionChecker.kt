package com.njves.schoolnetwork.Models

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

open class ConnectionChecker(context: Context){
    private val mConnectivityManager: ConnectivityManager
    companion object{
        const val TAG = "ConnectionChecker"
    }
    init {
        mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    /**
     * https://gist.github.com/A7maDev/427694dcae675435ce53
     */
    open fun isNetworkConnectionAvailable(): Boolean {
        try {
            val networkInfo = mConnectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected && networkInfo.isAvailable
        } catch (e: Exception) {
            Log.e(TAG, "Getting internet connection status")
        }

        return false
    }
}