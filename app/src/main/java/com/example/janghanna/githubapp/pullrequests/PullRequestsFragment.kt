package com.example.janghanna.githubapp.pullrequests


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.issues.AssignedFragment
import com.example.janghanna.githubapp.issues.CreatedFragment
import com.example.janghanna.githubapp.issues.MentionedFragment
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
        val args = Bundle()
        args.putBoolean("pullRequest", true)
        adapter.addFragment(CreatedFragment().apply { arguments = args }, "Created")
        adapter.addFragment(AssignedFragment().apply { arguments = args }, "Assigned")
        adapter.addFragment(MentionedFragment().apply { arguments = args }, "Mentioned")
        viewPager.adapter = adapter
    }


}
