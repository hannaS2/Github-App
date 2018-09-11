package com.example.janghanna.githubapp.profile


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.getUserId
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.repository.RepositoryActivity
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.item_pinned_repository.view.*
import org.jetbrains.anko.startActivity
import org.jsoup.Jsoup
import java.io.IOException
import kotlin.properties.Delegates


class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        view.contributionsView.webViewClient = WebViewClient()
        val webSettings = view.contributionsView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        view.contributionsView.loadUrl("http://ghchart.rshah.org/${getUserId(this.context!!)}")  //https://github.com/2016rshah/githubchart-api

//        val mHandler = Handler()
//        graphParser(view.contributionsView, mHandler)


        val adapter = PinnedReposAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.pinnedRecyclerView.adapter = adapter
        view.pinnedRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.pinnedRecyclerView.layoutManager = layoutManager

        val eventCall = provideGithubApi(requireContext()).getPinnedRepositories(getUserId(requireContext()))
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
            }
        }, {
            Log.i("OverviewFragment", it.message.toString())
        })



        return view
    }

    private fun graphParser(view: WebView, handler: Handler) {  // Jsoup 파싱

        Thread(Runnable {
            try {
                val document = Jsoup.connect("https://github.com/${getUserId(this.context!!)}").get()
                val contributionsGraph = document.select("svg.js-calendar-graph-svg").first().outerHtml()

                handler.post {
                    view.loadData(contributionsGraph,"text/html","utf-8")
                }

            } catch (e : IOException) {
                e.printStackTrace()
            }
        }).start()
    }


}


class PinnedReposViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pinned_repository, parent, false))

class PinnedReposAdapter() : RecyclerView.Adapter<PinnedReposViewHolder>() {
    var items: List<Repository> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PinnedReposViewHolder = PinnedReposViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: PinnedReposViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            repositoryNameText.text = item.name
            item.description?.let {
                descriptionText.visibility = View.VISIBLE
                descriptionText.text = item.description

            }
            languageText.text = item.language
            languageImage.setColorFilter(Color.parseColor(getLanguageColor(context, item.language)))
            starText.text = item.star

            repositoryNameText.setOnClickListener {
                context.startActivity<RepositoryActivity>("repository" to item)
            }
        }


    }

}