package com.example.janghanna.githubapp.repository


import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.model.File
import com.example.janghanna.githubapp.api.model.Repository
import com.example.janghanna.githubapp.api.provideGithubApi
import com.example.janghanna.githubapp.ui.enqueue
import kotlinx.android.synthetic.main.fragment_files.view.*
import kotlinx.android.synthetic.main.item_file.view.*
import org.jetbrains.anko.startActivity
import java.lang.Double.parseDouble
import kotlin.properties.Delegates


class FilesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_files, container, false)

        val repo = arguments?.getSerializable("repo") as Repository
        val path = arguments?.getString("path", "").toString()
//        Log.i("Aaaaaa", repo.toString())
        Log.i("aaaaaa", path)
        view.filePathText.text = path

        val adapter = FileAdapter(fragmentManager)
        val layoutManager = LinearLayoutManager(requireContext())
        view.fileRecyclerView.adapter = adapter
        view.fileRecyclerView.layoutManager = layoutManager

        val eventCall = provideGithubApi(requireContext()).getSourceFile(repo.owner.id, repo.name, path)
        eventCall.enqueue({
            it.body()?.let {
                adapter.items = it
                view.fileProgressBar.visibility = View.GONE
            }
        }, {
            Log.i("FilesFragment", it.message.toString())
        })

        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener { _, p1, p2 ->
            if (p1 == KeyEvent.KEYCODE_BACK && p2?.action == KeyEvent.ACTION_DOWN) {
//                Log.i("aaaaa", "click")
                fragmentManager?.popBackStack()
                return@OnKeyListener true
            }
            false
        })

        return view
    }


}

class FileViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false))

class FileAdapter(private val fragmentManager: FragmentManager?) : RecyclerView.Adapter<FileViewHolder>() {
    var items: List<File> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder = FileViewHolder(parent)

    override fun getItemCount() = items.count()

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {
            fileNameText.text = item.name
            fileImage.setImageDrawable(generateFileIcon(holder.itemView.context, item.type))
            if (item.type == "file") fileSizeText.text = ("${"%.2f".format(item.size/1000)} KB").toString()

            fileNameText.setOnClickListener {
                if (item.type == "file") {
                    context.startActivity<FileActivity>("url" to item.url, "path" to item.path)
                } else {
                    val fragment = FilesFragment()
                    val args = Bundle()
                    args.putString("path", item.path)
                    args.putSerializable("repo", RepositoryActivity.repo)
                    fragment.arguments = args

                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.file_layout, fragment)
                    transaction?.addToBackStack(null)
                    transaction?.commit()
                }
            }
        }

    }

    private fun generateFileIcon(context: Context, type: String): Drawable? {
        return when (type) {
            "file" -> ContextCompat.getDrawable(context, R.drawable.ic_file)
            "dir" -> ContextCompat.getDrawable(context, R.drawable.ic_file_directory)
            else -> ContextCompat.getDrawable(context, R.drawable.ic_file)
        }
    }

}