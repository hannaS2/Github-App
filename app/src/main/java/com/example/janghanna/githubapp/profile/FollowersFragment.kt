package com.example.janghanna.githubapp.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.GlideApp
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.User
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.repository.RepositoryActivity
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_follow.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import org.jetbrains.anko.startActivity
import kotlin.properties.Delegates


class FollowersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_follow, container, false)

        val user = arguments?.getString("user", null)

        val adapter = UserAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.followRecyclerView.adapter = adapter
        view.followRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.followRecyclerView.layoutManager = layoutManager

        val eventCall = user?.let{ provideGithubApi(requireContext()).getUserFollowers(user) }
                ?: run { provideGithubApi(requireContext()).getFollowers() }
//        val eventCall = provideGithubApi(requireContext()).getFollowers()
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
            }
        }, {
            Log.i("FollowersFragment", it.message.toString())
        })

        return view
    }


}


class UserViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

class UserAdapter() : RecyclerView.Adapter<UserViewHolder>() {
    var items: List<User> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder = UserViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            userIdText.text = item.id
            GlideApp.with(this)
                    .load(item.image)
                    .centerCrop()
                    .into(followUserImageView)

            val userCall = provideGithubApi(context).getUserName(item.id)
            userCall.enqueue({
                it.body()?.let {
                    usernameText.text = it.name
                }
            }, {
                Log.i("FollowersFragment", it.message.toString())
            })

            userIdText.setOnClickListener {
                context.startActivity<UserActivity>("user" to item.id)
            }

        }


    }

}