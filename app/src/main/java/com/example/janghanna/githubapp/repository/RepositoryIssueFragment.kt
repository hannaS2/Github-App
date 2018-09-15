package com.example.janghanna.githubapp.repository


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.issues.IssuesTabFragment
import kotlinx.android.synthetic.main.fragment_issues_tab.view.*

class RepositoryIssueFragment : IssuesTabFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issues_tab, container, false)

        val repo = arguments?.getSerializable("repo") as Repository

        setupViewPager(view.issuesTabViewPager, "", repo, null)
        view.issuesTabTabLayout.setupWithViewPager(view.issuesTabViewPager)

        setOpenCloseTabText(view, null, repo, null)

        return view
    }


}
