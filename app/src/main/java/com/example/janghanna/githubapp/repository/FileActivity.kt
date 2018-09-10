package com.example.janghanna.githubapp.repository

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.janghanna.githubapp.R
import com.pddstudio.highlightjs.models.Language
import kotlinx.android.synthetic.main.activity_file.*
import java.net.URL

class FileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        val code = intent.extras.getString("url", "").toString()
        val path = intent.extras.getString("path", "").toString()

        setSupportActionBar(fileToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = path.split("/").last()
        }
        fileToolbar.setNavigationOnClickListener { onBackPressed() }

        with(codeView) {
            visibility = View.VISIBLE
            setSource(URL(code))  // PDDStudio/highlightjs-android
            highlightLanguage = Language.AUTO_DETECT
            setZoomSupportEnabled(true)
            setShowLineNumbers(true)
        }

    }
}
