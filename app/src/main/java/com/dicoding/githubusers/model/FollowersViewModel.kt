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

class FollowersViewModel : ViewModel() {
    val listFollowersUser = MutableLiveData<ArrayList<User>>()

    fun setListFollowersUser(username: String) {
        ApiConfig.instanceApi
            .getFollowersUsers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        listFollowersUser.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getFollowersUser(): LiveData<ArrayList<User>> {
        return listFollowersUser
    }
}