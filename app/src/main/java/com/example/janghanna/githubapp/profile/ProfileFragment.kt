package com.example.janghanna.githubapp.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        setupViewPager(view.profileViewPager)
        view.profileTabLayout.setupWithViewPager(view.profileViewPager)

        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter: ViewPagerAdapter = childFragmentManager.let { ViewPagerAdapter(it) }
        adapter.addFragment(OverviewFragment(), "Overview")
        adapter.addFragment(RepositoriesFragment(), "Repositories")
        adapter.addFragment(StarsFragment(), "Stars")
        adapter.addFragment(FollowersFragment(), "Followers")
        adapter.addFragment(FollowingFragment(), "Following")
        viewPager.adapter = adapter
    }


}
