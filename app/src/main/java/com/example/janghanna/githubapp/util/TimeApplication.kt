package com.example.janghanna.githubapp.util

import android.app.Application
import android.util.Log
import net.danlew.android.joda.JodaTimeAndroid

class TimeApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // 앱이 처음 구동할 때 생성되는 인스턴스
        JodaTimeAndroid.init(this)
        Log.d("TimeApplication", "onCreate")
    }
}