package com.example.janghanna.githubapp.profile


import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.api.getUserId
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup
import java.io.IOException


class OverviewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        view.contributionsView.webViewClient = WebViewClient()
        val webSettings = view.contributionsView.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        view.contributionsView.loadUrl("http://ghchart.rshah.org/40a5fc/${getUserId(this.context!!)}")  //https://github.com/2016rshah/githubchart-api

//        val mHandler = Handler()
//        graphParser(view.contributionsView, mHandler)



        return view
    }

    private fun graphParser(view: WebView, handler: Handler) {

        Thread(Runnable {
            try {
                val document = Jsoup.connect("https://github.com/${getUserId(this.context!!)}").get()
                val contributionsGraph = document.select("svg.js-calendar-graph-svg").first().outerHtml()

                handler.post {
                    view.loadData(contributionsGraph,"text/html","utf-8")
                }

            } catch (e : IOException) {
                e.printStackTrace()
            }
        }).start()

    }


}
