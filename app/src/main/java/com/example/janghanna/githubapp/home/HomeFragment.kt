package com.example.janghanna.githubapp.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.enqueue
import kotlinx.android.synthetic.main.item_home.view.*
import kotlin.properties.Delegates


class HomeFragment : Fragment() {

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val itemView = inflater.inflate(R.layout.fragment_home, container, false)

        val items: List<HomeItem> = listOf()

        val eventCall = provideGithubApi(this.context!!).browseActivity("eunjeongS2")
        eventCall.enqueue({
            it.body()?.let {
                //Log.i(TAG, it.toString())

                it.forEach {
                    Log.i(HomeFragment.TAG, it.toString())
                }
            }
        }, {
            Log.i(HomeFragment.TAG, it.message.toString())
        })



        return itemView
    }


}

data class HomeItem(val user: String, val type: String, val content: String, val date: String, val image: String)

class HomeViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))

class ChatAdapter : RecyclerView.Adapter<HomeViewHolder>() {
    var items: List<HomeItem> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder = HomeViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            usernameText.text = item.user
            contentText.text = item.content
            updateText.text = item.date

            GlideApp.with(this)
                    .load(item.image)
                    .centerCrop()
                    .into(userImageView)

        }
    }

}

@GlideModule
class ChatAppGlideModule : AppGlideModule()

