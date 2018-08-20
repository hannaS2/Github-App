package com.example.janghanna.githubapp.api.model

import com.google.gson.annotations.SerializedName

data class Auth(
        @field:SerializedName("access_token")
        val accessToken: String,
        @field:SerializedName("token_type")
        val tokenType: String)

data class GithubRepo(@field:SerializedName("full_name") val fullName: String)
data class RepoSearchResponse(@field:SerializedName("total_count") val totalCount: Int,
                              val items: List<GithubRepo>)


data class Event(val actor: Actor, val type: String, val repo: Repo, @field:SerializedName("created_at") val date: String)
data class Actor(val login: String, @field:SerializedName("avatar_url") val image: String)
data class Repo(@field:SerializedName("name") val repoName: String)
