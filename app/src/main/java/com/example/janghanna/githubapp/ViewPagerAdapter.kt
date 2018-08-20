package com.example.janghanna.githubapp

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val fragments = LinkedHashMap<Fragment, String>()

    override fun getItem(position: Int): Fragment {
        return fragments.keys.elementAt(position)
    }

    override fun getCount(): Int {
        return fragments.keys.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragments[fragment] = title
    }

    override fun getPageTitle(position: Int): CharSequence? {
//        return fragments.values.elementAt(position) // 아이콘+글씨
        return null
    }
}