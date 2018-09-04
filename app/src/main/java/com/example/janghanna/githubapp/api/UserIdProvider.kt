package com.example.janghanna.githubapp.api

import android.content.Context
import android.preference.PreferenceManager

const val KEY_USER = "com.example.janghanna.githubapp.user"

fun updateUserId(context: Context, id: String) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_USER, id)
            .apply()
}

fun getUserId(context: Context): String {
    return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_USER, "")
}
