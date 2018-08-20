package com.example.janghanna.githubapp.api

import com.example.janghanna.githubapp.api.model.Event
import com.example.janghanna.githubapp.api.model.RepoSearchResponse
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("search/repositories")
    fun searchRepository(@Query("q") query: String): Call<RepoSearchResponse>

    @GET("users/{user}/received_events/public")
    fun browseActivity(@Path("user") user: String): Call<List<Event>>
}