package com.njves.schoolnetwork.presenter.profile

import android.content.Intent
import androidx.fragment.app.DialogFragment

interface ProfileNavigator {
    fun showDialogSelect(dialog : DialogFragment)
    fun requestPhoto(intent: Intent, requestCode: Int)
}
