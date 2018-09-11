package com.example.janghanna.githubapp


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import com.example.janghanna.githubapp.api.model.Repository
import kotlin.properties.Delegates

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        Log.i("aaaa", "fragmentmenu")

        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("aaaa", "text submit")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("aaaa", "text editting...")
                return true
            }

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


        }
    }


}
