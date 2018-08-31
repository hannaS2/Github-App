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
import com.example.janghanna.githubapp.api.model.Issue
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_issues_tab.view.*


open class IssuesTabFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issues_tab, container, false)

//        setupViewPager(view.issuesTabViewPager)
//        view.issuesTabTabLayout.setupWithViewPager(view.issuesTabViewPager)

        return view
    }

    fun setupViewPager(viewPager: ViewPager) {
        val adapter: ViewPagerAdapter = childFragmentManager.let { ViewPagerAdapter(it) }
        adapter.addFragment(OpenCloseFragment(), "open")
        adapter.addFragment(OpenCloseFragment(), "closed")
        viewPager.adapter = adapter
    }

    fun setOpenCloseTabText(view: View, filter: String) {
        getOpenIssue(view, filter)
        getClosedIssue(view, filter)
    }

    private fun getOpenIssue(view: View, filter: String) {
        val openCall = provideGithubApi(this.context!!).getIssues(filter, "open")
        openCall.enqueue({
            it.body()?.let {
                view.issuesTabTabLayout.getTabAt(0)!!.text = "${it.size} Open"
                Log.i("aaa", it.toString())
            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })
    }

    private fun getClosedIssue(view: View, filter: String) {
        val closedCall = provideGithubApi(this.context!!).getIssues("created", "closed")
        closedCall.enqueue({
            it.body()?.let {
                view.issuesTabTabLayout.getTabAt(1)!!.text = "${it.size} Closed"
            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })
    }

}
