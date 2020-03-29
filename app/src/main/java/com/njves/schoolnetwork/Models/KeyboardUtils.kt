package com.njves.schoolnetwork.Models

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardUtils {
    companion object{
        fun hideKeyboard(activity : Activity){
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            if(activity.currentFocus != null){
                imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            }
        }
    }
}