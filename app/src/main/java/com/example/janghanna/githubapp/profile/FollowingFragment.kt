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
import kotlinx.android.synthetic.main.fragment_follow.view.*


class FollowingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_follow, container, false)

        val user = arguments?.getString("user", null)

        val adapter = UserAdapter(fragmentManager)
        val layoutManager = LinearLayoutManager(requireContext())
        view.followRecyclerView.adapter = adapter
        view.followRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        view.followRecyclerView.layoutManager = layoutManager

        val eventCall = user?.let{ provideGithubApi(requireContext()).getUserFollowing(user) }
                ?: run { provideGithubApi(requireContext()).getFollowing() }
//        val eventCall = provideGithubApi(requireContext()).getFollowing()
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
