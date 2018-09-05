package com.example.janghanna.githubapp.issues

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.HomeFragment
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Issue
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.calcDate
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_open_close.*
import kotlinx.android.synthetic.main.fragment_open_close.view.*
import kotlinx.android.synthetic.main.item_issue.view.*
import kotlin.properties.Delegates


class OpenCloseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_open_close, container, false)

        val adapter = IssueAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.issueRecyclerView.adapter = adapter
        view.issueRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.issueRecyclerView.layoutManager = layoutManager

        val filter = arguments?.get("filter").toString()
        val state = arguments?.get("state").toString()

        val openCall = provideGithubApi(this.context!!).getIssues(filter, state)
        openCall.enqueue({
            it.body()?.let {
                if(it.isEmpty()) {
                    noResultText.visibility = View.VISIBLE
                    noResultImage.visibility = View.VISIBLE
                }
                adapter.items = it
            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })

        return view
    }


}


class IssueViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_issue, parent, false))

class IssueAdapter : RecyclerView.Adapter<IssueViewHolder>() {
    var items: List<Issue> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder = IssueViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            issueRepoText.text = item.repository.fullName
            issueTitleText.text = item.title
            issueInfoText.text = generateIssueInfo(item)
        }
    }

    private fun generateIssueInfo(item: Issue): String {
        return when(item.state) {
            "open" -> "#${item.number} opened ${calcDate(item.date)} by ${item.user.id}"
            "closed" -> "#${item.number} by ${item.user.id} was closed ${calcDate(item.date)}"
            else -> ""
        }
    }

}