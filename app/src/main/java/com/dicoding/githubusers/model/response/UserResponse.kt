package com.dicoding.githubusers.model.response

import com.dicoding.githubusers.db.data.User

data class UserResponse(
    val items: ArrayList<User>
)
