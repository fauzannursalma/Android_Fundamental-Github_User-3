package com.dicoding.githubusers.db.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.githubusers.db.data.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert
    suspend fun addFavoriteUser(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun checkFavoriteUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun removeFavoriteUser(id: Int): Int
}