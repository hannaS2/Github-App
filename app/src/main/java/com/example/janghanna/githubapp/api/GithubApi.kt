package com.example.janghanna.githubapp.api

import com.example.janghanna.githubapp.api.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GithubApi {
    @GET("search/repositories")
    fun searchRepository(@Query("q") query: String): Call<RepoSearchResponse>

    @GET("users/{user}/received_events/public")
    fun browseActivity(@Path("user") user: String): Call<List<Event>>

    @GET("user")
    fun getMyUserInfo(@Query("access_token") query: String): Call<User>

    @GET("user/issues")
    fun getIssues(@Query("filter") filter: String?, @Query("state") state: String): Call<List<Issue>>

    @GET("repos/{user}/{repo}/issues")
    fun getRepoIssues(@Path("user") user: String, @Path("repo") repo: String, @Query("state") state: String): Call<List<Issue>>

    @GET("user/repos")
    fun getRepositories(): Call<List<Repository>>

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") user: String): Call<List<Repository>>

    @GET("users/{user}/repos")
    fun getPinnedRepositories(@Path("user") user: String): Call<List<Repository>>

    @GET("user/starred")
    fun getStarredRepositories(): Call<List<Repository>>

    @GET("users/{user}/starred")
    fun getUserStarredRepositories(@Path("user") user: String): Call<List<Repository>>

    @GET("user/followers")
    fun getFollowers(): Call<List<User>>

    @GET("user/following")
    fun getFollowing(): Call<List<User>>

    @GET("/users/{user}/followers")
    fun getUserFollowers(@Path("user") user: String): Call<List<User>>

    @GET("/users/{user}/following")
    fun getUserFollowing(@Path("user") user: String): Call<List<User>>

    @GET("repos/{user}/{repo}/readme")
    fun getReadMe(@Path("user") user: String, @Path("repo") repo: String): Call<ReadMe?>

    @GET("repos/{user}/{repo}/contents/{path}")
    fun getSourceFile(@Path("user") user: String, @Path("repo") repo: String, @Path("path") path: String): Call<List<File>>

    @GET("users/{user}")
    fun getUserInfo(@Path("user") user: String): Call<User>

    @GET("repos/{user}/{repo}/commits")
    fun getRepoCommits(@Path("user") user: String, @Path("repo") repo: String): Call<List<Commit>>

    @GET("repos/{user}/{repo}/stats/contributors")
    fun getRepoContributors(@Path("user") user: String, @Path("repo") repo: String): Call<List<Contributor>>

    @GET("/user/following/{user}")
    fun checkFollowingUser(@Path("user") user: String): Call<User>

    @PUT("/user/following/{user}")
    fun setFollowUser(@Path("user") user: String): Call<User>

    @DELETE("/user/following/{user}")
    fun setUnfollowUser(@Path("user") user: String): Call<User>

    @GET("/user/starred/{user}/{repo}")
    fun checkStarToRepo(@Path("user") user: String, @Path("repo") repo: String): Call<Repository>

    @PUT("/user/starred/{user}/{repo}")
    fun setStarToRepo(@Path("user") user: String, @Path("repo") repo: String): Call<Repository>

    @DELETE("/user/starred/{user}/{repo}")
    fun setUnstarToRepo(@Path("user") user: String, @Path("repo") repo: String): Call<Repository>

}