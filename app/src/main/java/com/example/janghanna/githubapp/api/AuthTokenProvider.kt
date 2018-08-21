package com.example.janghanna.githubapp.api

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.janghanna.githubapp.api.model.User
import com.example.janghanna.githubapp.enqueue

const val KEY_AUTH_TOKEN = "com.example.janghanna.githubapp.auth_token"
const val KEY_USER_INFO = "com.example.janghanna.githubapp.user_info"

fun updateToken(context: Context, token: String) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
}

fun getToken(context: Context) : String? {
    return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_AUTH_TOKEN, null)
}

fun getUserInfo(context: Context): User? {
    val userInfo = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_USER_INFO, null)
    Log.i("aaa", userInfo)
    val data = userInfo.split("/")

    return User(data[0], data[1], data[2])
}

