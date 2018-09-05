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

data class User(@field:SerializedName("login") val id: String,
                @field:SerializedName("avatar_url") val image: String,
                @field:SerializedName("name") val name: String)

data class Issue(@field:SerializedName("title") val title: String,
                 val repository: Repository,
                 val user: User,
                 @field:SerializedName("updated_at") val date: String,
                 val number: String,
                 val state: String)
data class Repository(val name: String,
                      @field:SerializedName("full_name") val fullName: String,
                      @field:SerializedName("stargazers_count") val star: String,
                      @field:SerializedName("forks") val fork: String,
                      @field:SerializedName("updated_at") val date: String,
                      val language: String,
                      @field:SerializedName("html_url") val url: String)
