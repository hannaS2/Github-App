package com.example.janghanna.githubapp.profile

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.util.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val user = intent.extras.getString("user", null)

        setSupportActionBar(userToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = user
        }
        userToolbar.setNavigationOnClickListener { onBackPressed() }

        setupViewPager(userViewPager, user)
        userTabLayout.setupWithViewPager(userViewPager)


    }

    private fun setupViewPager(viewPager: ViewPager, userId: String) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        val args = Bundle()
        args.putString("user", userId)

        adapter.addFragment(UserProfileFragment().apply { arguments = args }, "Overview")
        adapter.addFragment(RepositoriesFragment().apply { arguments = args }, "Repositories")
        adapter.addFragment(StarsFragment().apply { arguments = args }, "Stars")
        adapter.addFragment(FollowersFragment().apply { arguments = args }, "Followers")
        adapter.addFragment(FollowingFragment().apply { arguments = args }, "Following")
        viewPager.adapter = adapter
    }
}
