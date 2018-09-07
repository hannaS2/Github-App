package com.example.janghanna.githubapp.repository


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.janghanna.githubapp.R
import com.pddstudio.highlightjs.models.Language
import kotlinx.android.synthetic.main.fragment_file.view.*
import java.net.URL


class FileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_file, container, false)

        val code = arguments?.getString("url", "").toString()

        view.codeView.setSource(URL(code))  // PDDStudio/highlightjs-android
        view.codeView.highlightLanguage = Language.AUTO_DETECT
        view.codeView.setZoomSupportEnabled(true)
        view.codeView.setShowLineNumbers(true)

        return view
    }


}
