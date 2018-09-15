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
import com.example.janghanna.githubapp.api.model.Issue
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import com.example.janghanna.githubapp.util.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_issues_tab.view.*

open class IssuesTabFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issues_tab, container, false)

        return view
    }

    fun setupViewPager(viewPager: ViewPager, type: String, repo: Repository?, pull: Boolean?) {
        val adapter: ViewPagerAdapter = childFragmentManager.let { ViewPagerAdapter(it) }

        val openFragment = OpenCloseFragment()   // listener로 해보
        val openArgs = setArguments(type, "open", repo, pull)
        openFragment.arguments = openArgs

        val closeFragment = OpenCloseFragment()
        val closeArgs = setArguments(type, "closed", repo, pull)
        closeFragment.arguments = closeArgs

        adapter.addFragment(openFragment, "open")
        adapter.addFragment(closeFragment, "closed")
        viewPager.adapter = adapter
    }

    private fun setArguments(filter: String, state: String, repo: Repository?, pull: Boolean?): Bundle {
        val args = Bundle()
        args.putString("filter", filter)
        args.putString("state", state)
        args.putSerializable("repo", repo)
        pull?.let { args.putBoolean("pullRequest", it) }
        return args
    }

    fun setOpenCloseTabText(view: View, filter: String?, repo: Repository?, pull: Boolean?) {
        getOpenClosedIssue(view, "Open", filter, repo, pull)
        getOpenClosedIssue(view, "Closed", filter, repo, pull)
    }

    private fun getOpenClosedIssue(view: View, type: String, filter: String?, repo: Repository?, pull: Boolean?) {
//        Log.i("aaaaa", pull.toString())
        val typeNum = if(type == "Open") 0 else 1

        val openCall = repo?.let { provideGithubApi(requireContext()).getRepoIssues(repo.owner.id, repo.name, "open") }
                ?: run { provideGithubApi(this.context!!).getIssues(filter, type.toLowerCase()) }
        openCall.enqueue({
            it.body()?.let {

                pull?.let { pull ->
                    if (pull) {
                        val pullRequestList = mutableListOf<Issue>()
                        for (issue in it) {
                            if (issue.pullRequest != null) {
                                pullRequestList.add(issue)
                            }
                        }
                        view.issuesTabTabLayout.getTabAt(typeNum)!!.text = "${pullRequestList.size} $type"
                    }
                } ?: run {
                    view.issuesTabTabLayout.getTabAt(typeNum)!!.text = "${it.size} $type"
                }

            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })
    }

}
