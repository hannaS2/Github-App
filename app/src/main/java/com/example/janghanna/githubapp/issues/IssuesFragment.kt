package com.example.janghanna.githubapp.issues


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.*
import kotlinx.android.synthetic.main.fragment_issues.view.*


class IssuesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_issues, container, false)

        setupViewPager(view.issuesViewPager)
        view.issuesTabLayout.setupWithViewPager(view.issuesViewPager)

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
