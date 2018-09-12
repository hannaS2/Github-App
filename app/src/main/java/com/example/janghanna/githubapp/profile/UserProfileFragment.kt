package com.example.janghanna.githubapp.profile


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.janghanna.githubapp.GlideApp
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.view.*
import org.jetbrains.anko.support.v4.toast


class UserProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val user = arguments?.getString("user", null)

//        var following = false
//        val followingCall = user?.let { provideGithubApi(requireContext()).checkFollowingUser(it) }
//        followingCall?.enqueue({
//            following = it.code() == 204
//        }, {
//            Log.i("UserProfileFragment", it.message.toString())
//        })


        val userCall = user?.let { provideGithubApi(requireContext()).getUserInfo(it) }
        userCall?.enqueue({
            it.body()?.let {
                with(view) {
                    userProfileNameText.text = it.name
                    userProfileIdText.text = it.id
                    userProfileEmailText.text = it.email
                    followersText.text = it.followers.toString()
                    followingText.text = it.following.toString()
                    GlideApp.with(this)
                            .load(it.image)
                            .centerCrop()
                            .into(userProfileImage)
                    userProfileImage.clipToOutline = true

                    val followingCall = user?.let { provideGithubApi(requireContext()).checkFollowingUser(it) }
                    followingCall?.enqueue({
                        followButton.text = if(it.code() == 204) "Unfollow" else "Follow"
                    }, {
                        Log.i("UserProfileFragment", it.message.toString())
                    })
//                    followButton.text = if (following) "Unfollow" else "Follow"

                    user_layout.visibility = View.VISIBLE
                    userProgressBar.visibility = View.GONE
                }

            }
        }, {
            Log.i("UserProfileFragment", it.message.toString())
        })


        view.userContributions.webViewClient = WebViewClient()
        val webSettings = view.userContributions.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        view.userContributions.loadUrl("http://ghchart.rshah.org/$user")

        view.followButton.setOnClickListener {
            view.followButton.text.apply {
                if (this == "Follow") {
                    val followCall = user?.let { provideGithubApi(requireContext()).setFollowUser(it) }
                    followCall?.enqueue({
                        if (it.code() == 204) {
                            view.followButton.text = "Unfollow"
                            followersText.text = (followersText.text.toString().toInt() + 1).toString()
                            toast("follow")
                        }
                    }, {
                        Log.i("UserProfileFragment", it.message.toString())
                    })
                    view.followButton.text = "Unfollow"
                } else {
                    val followCall = user?.let { provideGithubApi(requireContext()).setUnfollowUser(it) }
                    followCall?.enqueue({
                        if (it.code() == 204) {
                            view.followButton.text = "Follow"
                            followersText.text = (followersText.text.toString().toInt() - 1).toString()
                            toast("unfollow")
                        }
                    }, {
                        Log.i("UserProfileFragment", it.message.toString())
                    })
                    view.followButton.text = "Follow"
                }
            }


        }

        return view
    }


}
