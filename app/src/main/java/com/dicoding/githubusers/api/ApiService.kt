package com.dicoding.githubusers.api

import com.dicoding.githubusers.model.response.DetailUserResponse
import com.dicoding.githubusers.db.data.User
import com.dicoding.githubusers.model.response.UserResponse
import retrofit2.Call
import com.dicoding.githubusers.BuildConfig.TOKEN_API
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token $TOKEN_API")
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>


    @GET("users/{username}")
    @Headers("Authorization: token $TOKEN_API")
    fun getDetailUsers(
        @Path("username") username: String
    ): Call<DetailUserResponse>


    @GET("users/{username}/followers")
    @Headers("Authorization: token $TOKEN_API")
    fun getFollowersUsers(
        @Path("username") username: String
    ): Call<ArrayList<User>>


    @GET("users/{username}/following")
    @Headers("Authorization: token $TOKEN_API")
    fun getFollowingUsers(
        @Path("username") username: String
    ): Call<ArrayList<User>>


}