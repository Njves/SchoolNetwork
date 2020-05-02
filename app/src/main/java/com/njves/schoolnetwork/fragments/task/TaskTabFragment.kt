package com.njves.schoolnetwork.fragments.task

import com.njves.schoolnetwork.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.njves.schoolnetwork.adapter.TaskPagerAdapter
import com.njves.schoolnetwork.fragments.task.TaskFragment


class TaskTabFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_task_tab, container, false)
        val tabLayout = v.findViewById<TabLayout>(R.id.tabLayout)
        val pager = v.findViewById<ViewPager>(R.id.pager)
        pager.adapter = TaskPagerAdapter(childFragmentManager, listOf(
            TaskFragment.newInstance(
                TaskFragment.FLAG_GET), TaskFragment.newInstance(
                TaskFragment.FLAG_GET_MY)))
        tabLayout.setupWithViewPager(pager)
        return v
    }
}