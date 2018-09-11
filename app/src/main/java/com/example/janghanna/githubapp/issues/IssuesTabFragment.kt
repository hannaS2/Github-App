package com.example.janghanna.githubapp.issues


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.HomeFragment
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.util.ViewPagerAdapter
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_issues_tab.view.*

open class IssuesTabFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issues_tab, container, false)

//        setupViewPager(view.issuesTabViewPager, "")
//        view.issuesTabTabLayout.setupWithViewPager(view.issuesTabViewPager)

        return view
    }

    fun setupViewPager(viewPager: ViewPager, type: String, repo: Repository?) {
        val adapter: ViewPagerAdapter = childFragmentManager.let { ViewPagerAdapter(it) }

        val openFragment = OpenCloseFragment()   // listener로 해보
        val openArgs = setArguments(type, "open", repo)
        openFragment.arguments = openArgs

        val closeFragment = OpenCloseFragment()
        val closeArgs = setArguments(type, "closed", repo)
        closeFragment.arguments = closeArgs

        adapter.addFragment(openFragment, "open")
        adapter.addFragment(closeFragment, "closed")
        viewPager.adapter = adapter
    }

    private fun setArguments(filter: String, state: String, repo: Repository?): Bundle {
        val args = Bundle()
        args.putString("filter", filter)
        args.putString("state", state)
        args.putSerializable("repo", repo)
        return args
    }

    fun setOpenCloseTabText(view: View, filter: String?, repo: Repository?) {
        getOpenIssue(view, filter, repo)
        getClosedIssue(view, filter, repo)
    }

    private fun getOpenIssue(view: View, filter: String?, repo: Repository?) {
//        val openCall = provideGithubApi(this.context!!).getIssues(filter, "open")
        val openCall = repo?.let { provideGithubApi(requireContext()).getRepoIssues(repo.owner.id, repo.name, "open") }
                ?: run { provideGithubApi(this.context!!).getIssues(filter, "open") }
        openCall.enqueue({
            it.body()?.let {
                view.issuesTabTabLayout.getTabAt(0)!!.text = "${it.size} Open"
            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })
    }

    private fun getClosedIssue(view: View, filter: String?, repo: Repository?) {
        val closedCall = repo?.let { provideGithubApi(requireContext()).getRepoIssues(repo.owner.id, repo.name, "closed") }
                ?: run { provideGithubApi(this.context!!).getIssues(filter, "closed") }
        closedCall.enqueue({
            it.body()?.let {
                view.issuesTabTabLayout.getTabAt(1)!!.text = "${it.size} Closed"
            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })
    }


}
