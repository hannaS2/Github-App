package com.example.janghanna.githubapp.issues

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import kotlinx.android.synthetic.main.fragment_open_close.view.*
import kotlin.properties.Delegates


class OpenCloseFragment : Fragment() {

    companion object {
        val TAG = OpenCloseFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_open_close, container, false)

        val adapter = IssueAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.issueRecyclerView.adapter = adapter
        view.issueRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.issueRecyclerView.layoutManager = layoutManager

        return view
    }


}


data class Issue(val content: String, val info: String)

class IssueViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))

class IssueAdapter : RecyclerView.Adapter<IssueViewHolder>() {
    var items: List<Issue> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder = IssueViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {


        }
    }


}