package com.njves.schoolnetwork.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.R

class SchoolAdapter(context: Context, objects : Array<out School>) :
    ArrayAdapter<School>(context,R.layout.support_simple_spinner_dropdown_item,objects)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val school = getItem(position)
        //convertView?.findViewById<>()
        return convertView!!
    }
}