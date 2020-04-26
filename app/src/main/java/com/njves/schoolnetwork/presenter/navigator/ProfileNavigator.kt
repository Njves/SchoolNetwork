package com.njves.schoolnetwork.presenter.navigator

import android.content.Intent
import androidx.fragment.app.DialogFragment

interface ProfileNavigator {
    fun showDialogSelect(dialog : DialogFragment)
    fun requestPhoto(intent: Intent, requestCode: Int)
}
