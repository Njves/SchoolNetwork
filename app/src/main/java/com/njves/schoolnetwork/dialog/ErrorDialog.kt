package com.njves.schoolnetwork.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.R

class ErrorDialog : DialogFragment() {

    var errorText : String? = null
    companion object{
        fun newInstance(errorText : String?) : ErrorDialog {
            val instance = ErrorDialog()
            val bundle = Bundle()
            // Если текст пустой следственно ошибка неизвестная
            bundle.putString("error_text", errorText)
            instance.arguments = bundle
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errorText = arguments?.getString("error_text", "")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Создаем билдер диалог
        val dialogBuilder = AlertDialog.Builder(activity)
        // Вставляем надпись ошибка
        dialogBuilder.setTitle(resources.getString(R.string.dialog_error_title))
        dialogBuilder.setMessage(errorText)
        dialogBuilder.setNeutralButton(R.string.dialog_submit, { dialog, which ->

        })
        return dialogBuilder.create()
    }
}