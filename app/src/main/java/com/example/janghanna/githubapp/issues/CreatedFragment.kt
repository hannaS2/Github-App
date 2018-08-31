package com.example.janghanna.githubapp.issues


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.HomeFragment

import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.ViewPagerAdapter
import com.example.janghanna.githubapp.api.getUserId
import com.example.janghanna.githubapp.api.model.Issue
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_issues_tab.*
import kotlinx.android.synthetic.main.fragment_issues_tab.view.*


class CreatedFragment : IssuesTabFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_issues_tab, container, false)

        setupViewPager(view.issuesTabViewPager)
        view.issuesTabTabLayout.setupWithViewPager(view.issuesTabViewPager)

        setOpenCloseTabText(view, "created")

        return view
    }

}
