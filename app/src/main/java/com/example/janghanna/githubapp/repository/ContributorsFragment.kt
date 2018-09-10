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
import com.example.janghanna.githubapp.api.model.Contributor
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_contributors.view.*
import kotlinx.android.synthetic.main.item_contributor.view.*
import kotlin.properties.Delegates


class ContributorsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contributors, container, false)

        val repo = arguments?.getSerializable("repo") as Repository

        view.contributorProgressBar.visibility = View.VISIBLE

        val adapter = ContributorAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        view.contributorRecyclerView.adapter = adapter
        view.contributorRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        view.contributorRecyclerView.layoutManager = layoutManager

        val eventCall = provideGithubApi(requireContext()).getRepoContributors(repo.owner.id, repo.name)
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
                view.contributorProgressBar.visibility = View.INVISIBLE
            }
        }, {
            Log.i("ContributorsFragment", it.message.toString())
        })


        return view
    }


}

class ContributorViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_contributor, parent, false))

class ContributorAdapter : RecyclerView.Adapter<ContributorViewHolder>() {
    var items: List<Contributor> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorViewHolder = ContributorViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: ContributorViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            contributorText.text = item.user.id
            commitCountText.text = "${item.count} commits"
            additionText.text = "${item.stats.sumBy { it.addition }} ++"
            deletionText.text = "${item.stats.sumBy { it.deletion }} --"
            GlideApp.with(this)
                    .load(item.user.image)
                    .centerCrop()
                    .into(contributorImage)

        }
    }

}