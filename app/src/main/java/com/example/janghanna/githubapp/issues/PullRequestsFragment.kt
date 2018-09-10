package com.example.janghanna.githubapp.issues


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.util.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_pull_requests.view.*

class PullRequestsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pull_requests, container, false)

        setupViewPager(view.pullRequestsViewPager)
        view.pullRequestsTabLayout.setupWithViewPager(view.pullRequestsViewPager)

        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter: ViewPagerAdapter = childFragmentManager.let { ViewPagerAdapter(it) }
        adapter.addFragment(CreatedFragment(), "Created")
        adapter.addFragment(AssignedFragment(), "Assigned")
        adapter.addFragment(MentionedFragment(), "Mentioned")
        viewPager.adapter = adapter
    }


}
