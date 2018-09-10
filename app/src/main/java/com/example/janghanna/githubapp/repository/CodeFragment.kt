package com.example.janghanna.githubapp.repository


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.janghanna.githubapp.R
import com.example.janghanna.githubapp.util.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_code.view.*


class CodeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_code, container, false)

        setupViewPager(view.codeViewPager)
        view.codeTabLayout.setupWithViewPager(view.codeViewPager)

        return view
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(setDataToFragment(InfoFragment()), "Info")
        adapter.addFragment(setDataToFragment(FilesFragment()), "Files")
        adapter.addFragment(setDataToFragment(CommitsFragment()), "Commits")
        adapter.addFragment(setDataToFragment(ReleasesFragment()), "Releases")
        adapter.addFragment(setDataToFragment(ContributorsFragment()), "Contributors")
        viewPager.adapter = adapter
    }

    private fun setDataToFragment(fragment: Fragment): Fragment {
        val args = Bundle()
        args.putSerializable("repo", RepositoryActivity.repo)
        fragment.arguments = args
        return fragment
    }

}
