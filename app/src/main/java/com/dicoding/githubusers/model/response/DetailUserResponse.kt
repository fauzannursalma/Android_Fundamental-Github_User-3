package com.dicoding.githubusers.model.response

import com.google.gson.annotations.SerializedName


data class DetailUserResponse(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("Int")
    val id: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("location")
    val location: String,

    @field:SerializedName("public_repos")
    val publicRepos: Int,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("followers")
    val followers: Int
)
