package com.example.janghanna.githubapp.api

import com.example.janghanna.githubapp.api.model.*
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

    @GET("user")
    fun getMyUserInfo(@Query("access_token") query: String): Call<User>

    @GET("user/issues")
    fun getIssues(@Query("filter") filter: String, @Query("state") state: String): Call<List<Issue>>

    @GET("user/repos")
    fun getRepositories(): Call<List<Repository>>

    @GET("users/{user}/repos")
    fun getPinnedRepositories(@Path("user") user: String): Call<List<Repository>>

    @GET("user/starred")
    fun getStarredRepositories(): Call<List<Repository>>

    @GET("user/followers")
    fun getFollowers(): Call<List<User>>

    @GET("user/following")
    fun getFollowing(): Call<List<User>>

    @GET("users")
    fun getUserInfo(@Path("user") user: String): Call<User>


}