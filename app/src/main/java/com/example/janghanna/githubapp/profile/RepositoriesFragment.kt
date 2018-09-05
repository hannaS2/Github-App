package com.example.janghanna.githubapp.profile


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.calcDate
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_repositories.view.*
import kotlinx.android.synthetic.main.item_repository.view.*
import kotlin.properties.Delegates


class RepositoriesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_repositories, container, false)

        val adapter = RepositoryAdapter()
        val layoutManager = LinearLayoutManager(this.context)
        view.repositoryRecyclerView.adapter = adapter
        view.repositoryRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.repositoryRecyclerView.layoutManager = layoutManager

        val eventCall = provideGithubApi(this.context!!).getRepositories()
        eventCall.enqueue({
            it.body()?.let {
//                Log.i("aaaaa", it.toString())
                adapter.items = it
            }
        }, {
            Log.i("RepositoriesFragment", it.message.toString())
        })

        return view
    }


}


class RepositoryViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false))

class RepositoryAdapter : RecyclerView.Adapter<RepositoryViewHolder>() {
    var items: List<Repository> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder = RepositoryViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            repositoryText.text = item.name
            starText.text = item.star
            forkText.text = item.fork
            updateText.text = calcDate(item.date)
            languageText.text = item.language

            val color = getLanguageColor(item.language)
            Log.i("aaaa", color)
            languageImageView.setColorFilter(Color.parseColor(color))

        }
    }

    private fun getLanguageColor(language: String?): String {
        if(language != null) {
            return when (language) {
                "Java" -> "#b07219"
                "Kotlin" -> "#F18E33"
                "C" -> "#555555"
                "Python" -> "#3572A5"
                "C++" -> "#f34b7d"
                "Go" -> "#375eab"
                "HTML" -> "#e34c26"
                "JavaScript" -> "#f1e05a"
                "Objective-C" -> "#438eff"
                "TypeScript" -> "#2b7489"
                "Vue" -> "#2c3e50"
                "CSS" -> "#563d7c"
                "R" -> "#198CE7"
                else -> "#ffffff"
            }
        } else return "#ffffff"

    }

}