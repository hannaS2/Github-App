package com.example.janghanna.githubapp.repository


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.GlideApp
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Commit
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.calcDate
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_commits.view.*
import kotlinx.android.synthetic.main.item_commit.view.*
import kotlin.properties.Delegates


class CommitsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_commits, container, false)

        val repo = arguments?.getSerializable("repo") as Repository

        view.commitProgressBar.visibility = View.VISIBLE

        val adapter = CommitAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        view.commitRecyclerView.adapter = adapter
        view.commitRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        view.commitRecyclerView.layoutManager = layoutManager

        val eventCall = provideGithubApi(requireContext()).getRepoCommits(repo.owner.id, repo.name)
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
                view.commitProgressBar.visibility = View.INVISIBLE
            }
        }, {
            Log.i("CommitsFragment", it.message.toString())
        })

        return view
    }


}

class CommitViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_commit, parent, false))

class CommitAdapter() : RecyclerView.Adapter<CommitViewHolder>() {
    var items: List<Commit> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder = CommitViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            commitText.text = item.content.message
            committerText.text = item.author.id
            commitDateText.text = calcDate(item.content.author.date)
            GlideApp.with(this)
                    .load(item.author.image)
                    .centerCrop()
                    .into(committerImage)
        }
    }

}

