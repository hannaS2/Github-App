package com.example.janghanna.githubapp.ui

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.janghanna.githubapp.GlideApp
import com.example.janghanna.githubapp.MainHomeFragment
import com.example.janghanna.githubapp.OrganizationsFragment
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.getToken
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.api.updateUserId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close
        ) {}

        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.container, MainHomeFragment()).commit()

        navigationView.setNavigationItemSelectedListener {
            var fragment: Fragment = MainHomeFragment()

            when (it.itemId) {
                R.id.action_home -> {
                    toast("home")
                    fragment = MainHomeFragment()
                }
                R.id.action_org -> {
                    toast("organizations")
                    fragment = OrganizationsFragment()
                }
                R.id.action_pinned -> toast("pinned")
                R.id.action_trending -> toast("trending")
                R.id.action_gists -> toast("gists")
                R.id.action_bug -> toast("report bug")
                R.id.action_settings -> toast("settings")
            }

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setDrawerHeader()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)

        customSearchIcon(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_search -> {
                toast("검색버튼 클릭")
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun customSearchIcon(menu: Menu?) {
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        val icon: ImageView = searchView.findViewById(android.support.v7.appcompat.R.id.search_button)
        val text: TextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)
        icon.setColorFilter(Color.WHITE)
        text.setTextColor(Color.WHITE)
        searchView.queryHint = "Search..."
        text.setHintTextColor(Color.WHITE)

        searchView.setOnSearchClickListener {
            toolbarIcon.visibility = View.GONE
        }
        searchView.setOnCloseListener {
            toolbarIcon.visibility = View.VISIBLE
            false
        }
    }

    private fun setDrawerHeader() {
        val eventCall = getToken(this)?.let { provideGithubApi(this).getMyUserInfo(it) }
        eventCall?.enqueue({
            it.body()?.let {
                usernameTextView.text = it.name
                userIdTextView.text = it.id
                GlideApp.with(this)
                        .load(it.image)
                        .centerCrop()
                        .into(userImageView)

                updateUserId(this, it.id)
            }
        }, {
            Log.i("MainActivity", it.message.toString())
        })
    }


}

