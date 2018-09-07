package com.example.janghanna.githubapp

import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.Log
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.issues.IssuesFragment
import com.example.janghanna.githubapp.issues.PullRequestsFragment
import com.example.janghanna.githubapp.repository.CodeFragment
import com.example.janghanna.githubapp.repository.ProjectFragment
import kotlinx.android.synthetic.main.activity_repository.*

class RepositoryActivity : AppCompatActivity() {

    companion object {
        lateinit var repo: Repository
    }

    private val tabIcons = listOf<Int>(R.drawable.ic_code, R.drawable.ic_issue_opened, R.drawable.ic_pull_request, R.drawable.ic_project)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        repo = intent.extras.getSerializable("repository") as Repository

        setSupportActionBar(repositoryToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = repo.name
        }
        repositoryToolbar.setNavigationOnClickListener { onBackPressed() }

        setupViewPager(repositoryViewPager)
        repositoryTabLayout.setupWithViewPager(repositoryViewPager)
        setupTabIcons(repositoryTabLayout)

        repositoryViewPager.addOnPageChangeListener(object: TabLayout.TabLayoutOnPageChangeListener(repositoryTabLayout){})
        repositoryTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon?.clearColorFilter()
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                repositoryViewPager.currentItem = tab?.position ?: 0

                val tabIconColor: Int = ContextCompat.getColor(this@RepositoryActivity, R.color.colorAccent)
                tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }
        })

    }

    private fun setupTabIcons(tabLayout: TabLayout) {
        tabLayout.getTabAt(0)?.setIcon(tabIcons[0])
        tabLayout.getTabAt(1)?.setIcon(tabIcons[1])
        tabLayout.getTabAt(2)?.setIcon(tabIcons[2])
        tabLayout.getTabAt(3)?.setIcon(tabIcons[3])
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(CodeFragment(), "Code")
        adapter.addFragment(IssuesFragment(), "Issues")
        adapter.addFragment(PullRequestsFragment(), "Pull Requests")
        adapter.addFragment(ProjectFragment(), "Project")
        viewPager.adapter = adapter
    }




}

