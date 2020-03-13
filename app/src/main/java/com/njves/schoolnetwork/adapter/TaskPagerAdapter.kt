package com.njves.schoolnetwork.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class TaskPagerAdapter(fm : FragmentManager, private val fragmentList : List<Fragment>): FragmentStatePagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Полученные задачи"
            else -> "Отправленные задачи"
        }
    }
}