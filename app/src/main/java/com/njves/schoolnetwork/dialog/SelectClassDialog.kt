package com.njves.schoolnetwork.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.fragments.ProfileFragment
import com.njves.schoolnetwork.R

class SelectClassDialog : DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_class_choice, null)
        builder.setView(view)
        builder.setTitle("Choice class")

        val npNumber = view.findViewById<NumberPicker>(R.id.npClassNumber)
        val npChar = view.findViewById<NumberPicker>(R.id.npClassChar)
        npNumber.minValue = 1
        npNumber.maxValue = 11
        val alphabet = Array<String>(32){ i-> ('А'+i).toString()}

        npChar.minValue = 0
        npChar.maxValue = alphabet.size-1
        npChar.displayedValues = alphabet
        npNumber.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener {
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
            }

        })
        npChar.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener{
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
            }
        })
        builder.setPositiveButton("Выбрать") { dialog, which ->
            val intent = Intent()
            intent.putExtra("number", npNumber.value)
            intent.putExtra("char", alphabet[npChar.value])
            targetFragment?.onActivityResult(ProfileFragment.REQUEST_CODE_DIALOG_CLASS_SELECT, Activity.RESULT_OK,intent)
        }


        return builder.create()
    }
}