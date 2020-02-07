package com.njves.schoolnetwork.Fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.adapter.ReceiversAdapter

class ReceiversListFragment : Fragment() {
    lateinit var rvReceivers : RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_receivers_list, container, false)
        rvReceivers = v.findViewById(R.id.rvReceivers)
        val adapter = ReceiversAdapter(context, listOf<User>(User("test", "test", "test", "test", "test", 1)))
        rvReceivers.layoutManager = LinearLayoutManager(context)
        rvReceivers.adapter = adapter

        return view
    }
}