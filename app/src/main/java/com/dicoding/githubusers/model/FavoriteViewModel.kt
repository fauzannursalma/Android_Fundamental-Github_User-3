package com.dicoding.githubusers.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubusers.db.data.FavoriteUser
import com.dicoding.githubusers.db.room.FavoriteUserDao
import com.dicoding.githubusers.db.room.FavoriteUserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDB: FavoriteUserDatabase? = FavoriteUserDatabase.getDatabase(application)

    init {
        userDao = userDB?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }
}