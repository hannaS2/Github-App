package com.example.janghanna.githubapp.profile


import android.content.Context
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
import com.example.janghanna.githubapp.repository.RepositoryActivity
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.calcDate
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_repositories.view.*
import kotlinx.android.synthetic.main.item_repository.view.*
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import java.io.IOException
import kotlin.properties.Delegates


class RepositoriesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_repositories, container, false)

        val user = arguments?.getString("user")

        val adapter = RepositoryAdapter("repo")
        val layoutManager = LinearLayoutManager(this.context)
        view.repositoryRecyclerView.adapter = adapter
        view.repositoryRecyclerView.addItemDecoration(DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL))
        view.repositoryRecyclerView.layoutManager = layoutManager

        val eventCall = user?.let { provideGithubApi(requireContext()).getUserRepositories(user) }
                ?: run { provideGithubApi(requireContext()).getRepositories() }
//        val eventCall = provideGithubApi(requireContext()).getRepositories()
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
                view.repositoryProgressBar.visibility = View.GONE
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

class RepositoryAdapter(val type: String) : RecyclerView.Adapter<RepositoryViewHolder>() {
    var items: List<Repository> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder = RepositoryViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            repositoryText.text = if (type == "repo") item.name else item.fullName
            starText.text = item.star.toString()
            forkText.text = item.fork.toString()
            updateText.text = calcDate(item.date)
            languageText.text = item.language

            val color = getLanguageColor(context, item.language)
            languageImageView.setColorFilter(Color.parseColor(color))

            repositoryText.setOnClickListener {
                context.startActivity<RepositoryActivity>("repository" to item)
            }

        }
    }
}

fun loadLanguageColorJson(context: Context): String? {
    val json: String?
    try {
        val ins = context.assets.open("languageColor.json")
        val size = ins.available()
        val buffer = ByteArray(size)
        ins.read(buffer)
        ins.close()
        json = String(buffer)
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }

    return json
}

fun getLanguageColor(context: Context, language: String?): String? {
    val obj = JSONObject(loadLanguageColorJson(context))
    return language?.let { obj.get(language).toString() } ?: "#ffffff"
}

//fun getLanguageColor(language: String?): String? {
//
//    return language?.let {
//        when(it) {
//            "Java" -> "#b07219"
//            "Kotlin" -> "#F18E33"
//            "C" -> "#555555"
//            "Python" -> "#3572A5"
//            "C++" -> "#f34b7d"
//            "Go" -> "#375eab"
//            "HTML" -> "#e34c26"
//            "JavaScript" -> "#f1e05a"
//            "Objective-C" -> "#438eff"
//            "TypeScript" -> "#2b7489"
//            "Vue" -> "#2c3e50"
//            "CSS" -> "#563d7c"
//            "R" -> "#198CE7"
//            else -> "#ffffff"
//        }
//    } ?: "#ffffff"
//}