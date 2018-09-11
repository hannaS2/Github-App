package com.example.janghanna.githubapp.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_repositories.view.*
import kotlinx.android.synthetic.main.fragment_stars.view.*


class StarsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_stars, container, false)

        val user = arguments?.getString("user", null)

        val adapter = RepositoryAdapter("starredRepo")
        val layoutManager = LinearLayoutManager(this.context)
        view.starsRecyclerView.adapter = adapter
        view.starsRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.starsRecyclerView.layoutManager = layoutManager

        val eventCall = user?.let{ provideGithubApi(requireContext()).getUserStarredRepositories(user) }
                ?: run { provideGithubApi(requireContext()).getStarredRepositories() }
//        val eventCall = provideGithubApi(this.context!!).getStarredRepositories()
        eventCall.enqueue({
            it.body()?.let {
                // Log.i("aaaaa", it.toString())
                adapter.items = it
            }
        }, {
            Log.i("StarsFragment", it.message.toString())
        })

        return view
    }


}
