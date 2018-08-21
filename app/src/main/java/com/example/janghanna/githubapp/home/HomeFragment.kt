package com.example.janghanna.githubapp.home


import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
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
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.getToken
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.enqueue
import com.example.janghanna.githubapp.home.model.HomeModel
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

    private var items = mutableListOf<HomeItem>()
    private lateinit var model: HomeModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val itemView = inflater.inflate(R.layout.fragment_home, container, false)

        Log.i(TAG, "oncreate")

        val adapter = HomeAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        itemView.homeRecyclerView.adapter = adapter
        itemView.homeRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        itemView.homeRecyclerView.layoutManager = layoutManager

        val userCall = getToken(this.context!!)?.let { provideGithubApi(this.context!!).getUserInfo(it) }
        userCall?.enqueue({
            it.body()?.let {

                val eventCall = provideGithubApi(this.context!!).browseActivity(it.id)
                eventCall.enqueue({
                    it.body()?.let {
                        //Log.i(TAG, it.toString())

                        it.forEach {
                            val item = HomeItem(it.actor.login, it.type, it.repo.repoName, it.date, it.actor.image)
                            items.add(item)
                        }

                        Log.i(TAG, items.toString())
                        adapter.items = items

                    }
                }, {
                    Log.i(HomeFragment.TAG, it.message.toString())
                })

            }
        }, {
            Log.i(TAG, it.message.toString())
        })

        return itemView
    }

}

data class HomeItem(val user: String, val type: String, val repo: String, val date: String, val image: String)

class HomeViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))

class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {
    var items: List<HomeItem> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder = HomeViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = items[position]
        val itemType = generateContent(item.type, holder.itemView.context)
//        Log.i("HomeType", item.type)

        with(holder.itemView) {
            usernameText.text = item.user
            contentText.text = "${itemType.first} ${item.repo}"
            updateText.text = calcDate(item.date)
            typeImageView.setImageDrawable(itemType.second)

            GlideApp.with(this)
                    .load(item.image)
                    .centerCrop()
                    .into(userImage)

//            userImage.background = ShapeDrawable(OvalShape())
//            userImage.clipToOutline = true

        }
    }

    private fun calcDate(itemDate: String): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormat.forPattern("MMM dd, yyyy")
        val itemDateTime = LocalDateTime.parse(itemDate.substring(0, itemDate.length - 1))

        val dayDiff = Days.daysBetween(itemDateTime, now).days
        val hourDiff = Hours.hoursBetween(itemDateTime, now).hours
        val minuteDiff = Minutes.minutesBetween(itemDateTime, now).minutes
        return when {
            hourDiff <= 0 -> "$minuteDiff minutes ago"
            dayDiff == 0 -> "$hourDiff hours ago"
            dayDiff <= 7 -> "$dayDiff days ago"
            else -> itemDateTime.toString(formatter)
        }
    }

    private fun generateContent(itemType: String, context: Context): Pair<String, Drawable?> {
        return when(itemType) {
            "WatchEvent" -> Pair("starred", ContextCompat.getDrawable(context, R.drawable.ic_star))
            "CreateEvent" -> Pair("created repository at", ContextCompat.getDrawable(context, R.drawable.ic_repo))
            "MemberEvent" -> Pair("", ContextCompat.getDrawable(context, R.drawable.ic_organization))
            else -> Pair("", null)
        }
    }


}

@GlideModule
class ChatAppGlideModule : AppGlideModule()

