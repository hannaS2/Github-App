package com.example.janghanna.githubapp.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.example.janghanna.githubapp.*
import com.example.janghanna.githubapp.api.getToken
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.api.updateUserId
import com.example.janghanna.githubapp.repository.RepositoryActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.item_search_repository.view.*
import kotlinx.android.synthetic.main.search_results.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    val adapter = SearchAdapter()

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
                R.id.action_home -> fragment = MainHomeFragment()
                R.id.action_org -> fragment = OrganizationsFragment()
                R.id.action_pinned -> toast("pinned")
                R.id.action_trending -> toast("trending")
                R.id.action_gists -> toast("gists")
                R.id.action_bug -> toast("report bug")
                R.id.action_settings -> fragment = SettingsFragment()
            }

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        setDrawerHeader()

        val layoutManager = LinearLayoutManager(this)
        searchRecyclerView.adapter = adapter
        searchRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        searchRecyclerView.layoutManager = layoutManager

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
//                toast("검색버튼 클릭")
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
            searchResultView.visibility = View.VISIBLE
        }
        searchView.setOnCloseListener {
            toolbarIcon.visibility = View.VISIBLE
            searchResultView.visibility = View.GONE
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val githubApiCall = newText?.let { provideGithubApi(this@MainActivity).searchRepository(it) }
                githubApiCall?.enqueue({
                    it.body()?.let {
                        Log.i("MainActivity", "total_count: ${it.totalCount}")
//                        Log.i("MainActivity", it.items.toString())
                        if (it.items.isEmpty()) {
                            noRepoResultText.visibility = View.VISIBLE
                            noRepoResultImage.visibility = View.VISIBLE
                        } else {
                            noRepoResultText.visibility = View.INVISIBLE
                            noRepoResultImage.visibility = View.INVISIBLE
                        }
                        adapter.items = it.items
                    }

                }, {
                    Log.i("MainActivity", it.message.toString())
                })

                return true
            }

        })

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

class SearchViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_search_repository, parent, false))

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    var items: List<Repository> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder = SearchViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            searchedRepoText.text = item.fullName
            searchedRepoText.setOnClickListener { context.startActivity<RepositoryActivity>("repository" to item) }
        }
    }


}
