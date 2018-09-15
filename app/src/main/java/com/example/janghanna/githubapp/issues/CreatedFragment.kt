package com.example.janghanna.githubapp.issues


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Repository
import kotlinx.android.synthetic.main.fragment_issues_tab.view.*


class CreatedFragment : IssuesTabFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_issues_tab, container, false)

        val pull = arguments?.getBoolean("pullRequest")

        setupViewPager(view.issuesTabViewPager, "created", null, pull)
        view.issuesTabTabLayout.setupWithViewPager(view.issuesTabViewPager)

        setOpenCloseTabText(view, "created", null, pull)

        return view
    }

}
