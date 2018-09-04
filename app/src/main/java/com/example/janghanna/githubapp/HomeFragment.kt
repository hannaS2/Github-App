package com.example.janghanna.githubapp


import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.janghanna.githubapp.api.getUserId
import com.example.janghanna.githubapp.api.model.Event
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_home.view.*
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.LocalDateTime
import org.joda.time.Minutes
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates


class HomeFragment : Fragment() {

    companion object {
        val TAG = HomeFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        Log.i(TAG, "oncreate")

        val adapter = HomeAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.homeRecyclerView.adapter = adapter
        view.homeRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.homeRecyclerView.layoutManager = layoutManager

//        Log.i("HomeId", getUserId(this.context!!))

        val eventCall = provideGithubApi(this.context!!).browseActivity(getUserId(this.context!!))
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
            }
        }, {
            Log.i(TAG, it.message.toString())
        })

        return view
    }

}


class HomeViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {
    var items: List<Event> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder = HomeViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = items[position]
        val itemType = generateContent(item.type, holder.itemView.context)

        with(holder.itemView) {
            usernameText.text = item.actor.login
            contentText.text = "${itemType.first} ${item.repo.repoName}"
            updateText.text = calcDate(item.date)
            typeImageView.setImageDrawable(itemType.second)

            GlideApp.with(this)
                    .load(item.actor.image)
                    .centerCrop()
                    .into(userImage)
//            userImage.background = ShapeDrawable(OvalShape())
//            userImage.clipToOutline = true

        }
    }

    private fun generateContent(itemType: String, context: Context): Pair<String, Drawable?> {
        return when(itemType) {
            "WatchEvent" -> Pair("starred", ContextCompat.getDrawable(context, R.drawable.ic_star))
            "CreateEvent" -> Pair("created a repository", ContextCompat.getDrawable(context, R.drawable.ic_repo))
            "MemberEvent" -> Pair("", ContextCompat.getDrawable(context, R.drawable.ic_organization))
            "ForkEvent" -> Pair("forked from", ContextCompat.getDrawable(context, R.drawable.ic_repo_forked))
            else -> Pair("", null)
        }
    }

}

fun calcDate(itemDate: String): String {
    val now = LocalDateTime.now()
    val formatter = DateTimeFormat.forPattern("MMM dd, yyyy")
    val itemDateTime = LocalDateTime.parse(itemDate.substring(0, itemDate.length - 1))

    val dayDiff = Days.daysBetween(itemDateTime, now).days
    val hourDiff = Hours.hoursBetween(itemDateTime, now).hours
    val minuteDiff = Minutes.minutesBetween(itemDateTime, now).minutes
    return when {
        dayDiff == 0 && hourDiff == 0 -> "$minuteDiff minutes ago"
        dayDiff == 0 -> "$hourDiff hours ago"
        dayDiff <= 7 -> "$dayDiff days ago"
        else -> itemDateTime.toString(formatter)
    }
}


@GlideModule
class ChatAppGlideModule : AppGlideModule()

