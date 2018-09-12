package com.example.janghanna.githubapp.repository


import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.getUserId
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.calcDate
import com.example.janghanna.githubapp.profile.getLanguageColor
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_info.view.*
import org.jetbrains.anko.support.v4.toast


class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        val repo = arguments?.getSerializable("repo") as Repository
//        Log.i("Aaaaaaa", repo.toString())
        with(view) {
            repoUserText.text = repo.owner.id
            repoNameText.text = repo.name
            repoCreatedDate.text = "Created at ${calcDate(repo.createdDate)}"
            repoLatestDateText.text = "Latest Commit ${calcDate(repo.lastPushDate)}"
            repoWatchText.text = repo.watcher.toString()
            repoStarText.text = repo.star.toString()
            repoForkText.text = repo.fork.toString()
            repoLanguageText.text = repo.language
            val color = getLanguageColor(requireContext(), repo.language)
            repoLanguageImage.setColorFilter(Color.parseColor(color))
        }

        val eventCall = provideGithubApi(requireContext()).getReadMe(getUserId(requireContext()), repo.name)
        eventCall.enqueue({
            it.body()?.let {
                view.readMeText.text = it.name
                view.readMeMarkdownView.visibility = View.VISIBLE
                view.readMeMarkdownView.loadMarkdownFile(it.url)  // falnatsheh/MarkdownView
            } ?: run {
                view.noReadMeText.visibility = View.VISIBLE
                view.noReadMeImage.visibility = View.VISIBLE
            }
        }, {
            Log.i("InfoFragment", it.message.toString())
        })

        var starred = false
        val starCall = provideGithubApi(requireContext()).checkStarToRepo(repo.owner.id, repo.name)
        starCall.enqueue({
            Log.i("aaaa", it.code().toString())
            if (it.code() == 204) {
                starred = true
                view.repoStarImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), PorterDuff.Mode.SRC_IN)
            }
        }, {
            Log.i("InfoFragment", it.message.toString())
        })

        view.repoStarImage.setOnClickListener {
            if (starred) {
                val starCall = provideGithubApi(requireContext()).setUnstarToRepo(repo.owner.id, repo.name)
                starCall.enqueue({
                    if (it.code() == 204) {
                        toast("Unstar")
                        view.repoStarText.text = (view.repoStarText.text.toString().toInt() - 1).toString()
                        view.repoStarImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black), PorterDuff.Mode.SRC_IN)
                        starred = false
                    }
                }, {
                    Log.i("InfoFragment", it.message.toString())
                })

            } else {
                val starCall = provideGithubApi(requireContext()).setStarToRepo(repo.owner.id, repo.name)
                starCall.enqueue({
                    if (it.code() == 204) {
                        toast("Star")
                        view.repoStarText.text = (view.repoStarText.text.toString().toInt() + 1).toString()
                        view.repoStarImage.setColorFilter(ContextCompat.getColor(requireContext(), R.color.blue), PorterDuff.Mode.SRC_IN)
                        starred = true
                    }
                }, {
                    Log.i("InfoFragment", it.message.toString())
                })
            }

        }


        return view
    }


}
