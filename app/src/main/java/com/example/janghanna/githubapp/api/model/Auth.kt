package com.example.janghanna.githubapp.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Auth(
@field:SerializedName("access_token")
val accessToken: String,
@field:SerializedName("token_type")
val tokenType: String)

data class RepoSearchResponse(@field:SerializedName("total_count") val totalCount: Int,
                              val items: List<Repository>)

data class Event(val actor: Actor, val type: String, val repo: Repo, @field:SerializedName("created_at") val date: String)
data class Actor(val login: String, @field:SerializedName("avatar_url") val image: String)
data class Repo(@field:SerializedName("name") val repoName: String)

data class User(@field:SerializedName("login") val id: String,
                @field:SerializedName("avatar_url") val image: String,
                @field:SerializedName("name") val name: String?,
                val email: String?,
                val followers: Int?,
                val following: Int?)

data class Issue(@field:SerializedName("title") val title: String,
                 val repository: Repository?,
                 val user: User,
                 @field:SerializedName("updated_at") val date: String,
                 val number: String,
                 val state: String,
                 val pullRequest: PullRequest?)
data class PullRequest(val url: String)

data class Repository(val name: String,
                      val owner: Owner,
                      @field:SerializedName("full_name") val fullName: String?,
                      @field:SerializedName("stargazers_count") val star: Int,
                      @field:SerializedName("forks_count") val fork: Int,
                      @field:SerializedName("watchers_count") val watcher: Int,
                      @field:SerializedName("updated_at") val date: String,
                      @field:SerializedName("created_at") val createdDate: String,
                      @field:SerializedName("pushed_at") val lastPushDate: String,
                      val language: String,
                      val description: String?,
                      @field:SerializedName("html_url") val url: String) : Serializable
data class Owner(@field:SerializedName("login") val id: String): Serializable

data class ReadMe(val name: String,
                  @field:SerializedName("download_url") val url: String)

data class File(val name: String,
                  val path: String,
                  val type: String,
                  @field:SerializedName("download_url") val url: String)

data class Commit(val author: User,
                  @field:SerializedName("commit") val content: CommitContent)
data class CommitContent(val message: String, val author: Committer)
data class Committer(val date: String)

data class Contributor(@field:SerializedName("author") val user: User,
                       @field:SerializedName("total") val count: Int,
                       @field:SerializedName("weeks") val stats: List<WeekStats>)
data class WeekStats(@field:SerializedName("w") val startWeek: String,
                     @field:SerializedName("a") val addition: Int,
                     @field:SerializedName("d") val deletion: Int,
                     @field:SerializedName("c") val commitCount: Int)