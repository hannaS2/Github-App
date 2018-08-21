package com.example.janghanna.githubapp


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import com.example.janghanna.githubapp.home.HomeFragment
import com.example.janghanna.githubapp.home.IssuesFragment
import com.example.janghanna.githubapp.home.ProfileFragment
import com.example.janghanna.githubapp.home.PullRequestsFragment
import kotlinx.android.synthetic.main.fragment_main_home.view.*


class MainHomeFragment : Fragment() {

    private val tabIcons = listOf<Int>(R.drawable.ic_home, R.drawable.ic_issue_opened, R.drawable.ic_pull_request, R.drawable.ic_person)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val itemView = inflater.inflate(R.layout.fragment_main_home, container, false)

        setupViewPager(itemView.viewPager)
        itemView.tabLayout.setupWithViewPager(itemView.viewPager)
        setupTabIcons(itemView.tabLayout)

        itemView.viewPager.addOnPageChangeListener(object: TabLayout.TabLayoutOnPageChangeListener(itemView.tabLayout){})
        itemView.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.clearColorFilter()
                tab?.text = ""
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                itemView.viewPager.currentItem = tab?.position ?: 0

                val tabIconColor: Int = context?.let { ContextCompat.getColor(it, R.color.colorAccent) }!!
                tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)

                var iconText = "Home"
                when(tab?.position) {
                    0 -> iconText = "Home"
                    1 -> iconText = "Issues"
                    2 -> iconText = "Pull Requests"
                    3 -> iconText = "Profile"
                }
                tab?.text = iconText
            }
        })

        return itemView
    }

    private fun setupTabIcons(tabLayout: TabLayout) {
        tabLayout.getTabAt(0)?.setIcon(tabIcons[0])
        tabLayout.getTabAt(1)?.setIcon(tabIcons[1])
        tabLayout.getTabAt(2)?.setIcon(tabIcons[2])
        tabLayout.getTabAt(3)?.setIcon(tabIcons[3])
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter: ViewPagerAdapter = childFragmentManager.let { ViewPagerAdapter(it) }
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(IssuesFragment(), "Issues")
        adapter.addFragment(PullRequestsFragment(), "Pull Requests")
        adapter.addFragment(ProfileFragment(), "Profile")
        viewPager.adapter = adapter
    }





}
