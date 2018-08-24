package com.example.janghanna.githubapp.api

import android.content.Context
import android.preference.PreferenceManager

const val KEY_AUTH_TOKEN = "com.example.janghanna.githubapp.auth_token"
const val KEY_USER = "com.example.janghanna.githubapp.user"

fun updateToken(context: Context, token: String) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
}

fun getToken(context: Context) : String? {
    return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_AUTH_TOKEN, null)
}

fun getUserId(context: Context): String {
    return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_USER, "")
}

fun removeUserData(context: Context) {
    PreferenceManager.getDefaultSharedPreferences(context)
            .edit().remove(KEY_USER).apply()
}
