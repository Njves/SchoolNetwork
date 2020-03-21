package com.njves.schoolnetwork.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.R

class SubmitActionDialog(val MODE : Int): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
        // TODO: Название задачи
        dialog.setTitle("Задача")
        dialog.setMessage(getActionText(MODE))
        dialog.setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
            // TODO:Добавить callback
            //targetFragment.onActivityResult()
        }
        dialog.setNegativeButton("Нет"){ dialogInterface: DialogInterface, i: Int ->

        }
        return dialog.create()
    }

    private fun getActionText(MODE : Int) : String
    {
        when(MODE){
            MODE_DELETE->{
                return context?.resources?.getString(R.string.warning_action_delete, TEXT_DELETE)!!
            }

        }
        return "Вы действительно хотите это сделать?"
    }
    companion object{
        const val MODE_DELETE = 0
        const val TEXT_DELETE = "Удалить"
    }

}