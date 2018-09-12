package com.example.janghanna.githubapp.issues

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Issue
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.calcDate
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_open_close.view.*
import kotlinx.android.synthetic.main.item_issue.view.*
import kotlin.properties.Delegates


class OpenCloseFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_open_close, container, false)

        val repo = arguments?.getSerializable("repo") as Repository?

        val adapter = IssueAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.issueRecyclerView.adapter = adapter
        view.issueRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.issueRecyclerView.layoutManager = layoutManager

        val filter = arguments?.get("filter").toString()
        val state = arguments?.get("state").toString()

        // home의 issue에서는 run실행(repo가 null인 경우),
        // repository의 issue에서는 전달받은 repo의 issue(repo가 null이 아닌 경우)
        val openCall = repo?.let { provideGithubApi(requireContext()).getRepoIssues(repo.owner.id, repo.name, state) }
                ?: run { provideGithubApi(requireContext()).getIssues(filter, state) }
        openCall.enqueue({
//            Log.i("aaaa", it.code().toString())
            it.body()?.let {
                if (it.isEmpty()) {
                    view.noResultText.visibility = View.VISIBLE
                    view.noResultImage.visibility = View.VISIBLE
                }
                adapter.items = it
            }
        }, {
            Log.i("OpenCloseFragment", it.message.toString())
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
            issueRepoText.text = item.repository?.let { item.repository.fullName } ?: run {
                issueRepoText.visibility = View.INVISIBLE
                ""
            }
            issueTitleText.text = item.title
            issueInfoText.text = generateIssueInfo(item)
        }
    }

    private fun generateIssueInfo(item: Issue): String {
        return when (item.state) {
            "open" -> "#${item.number} opened ${calcDate(item.date)} by ${item.user.id}"
            "closed" -> "#${item.number} by ${item.user.id} was closed ${calcDate(item.date)}"
            else -> ""
        }
    }
}



