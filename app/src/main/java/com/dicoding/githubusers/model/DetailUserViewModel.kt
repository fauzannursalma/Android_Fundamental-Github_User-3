package com.dicoding.githubusers.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.githubusers.api.ApiConfig
import com.dicoding.githubusers.db.data.FavoriteUser
import com.dicoding.githubusers.db.room.FavoriteUserDao
import com.dicoding.githubusers.db.room.FavoriteUserDatabase
import com.dicoding.githubusers.model.response.DetailUserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val user: MutableLiveData<DetailUserResponse> = MutableLiveData<DetailUserResponse>()

    private var userDao: FavoriteUserDao?
    private var FavUserDB: FavoriteUserDatabase? = FavoriteUserDatabase.getDatabase(application)

    init {
        userDao = FavUserDB?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        ApiConfig.instanceApi
            .getDetailUsers(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getUserDetail(): LiveData<DetailUserResponse> {
        return user
    }

    fun addFavoriteUser(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(id, username, avatarUrl)
            userDao?.addFavoriteUser(user)
        }
    }

    suspend fun checkFavoriteUser(id: Int) = userDao?.checkFavoriteUser(id)

    fun removeFavoriteUser(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFavoriteUser(id)
        }
    }

}