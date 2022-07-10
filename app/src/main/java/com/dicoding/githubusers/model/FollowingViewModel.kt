package com.dicoding.githubusers.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubusers.api.ApiConfig
import com.dicoding.githubusers.db.data.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val listFollowingUsers = MutableLiveData<ArrayList<User>>()

    fun setListFollowingUsers(username: String) {
        ApiConfig.instanceApi
            .getFollowingUsers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        listFollowingUsers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getFollowingUsers(): LiveData<ArrayList<User>> {
        return listFollowingUsers
    }
}