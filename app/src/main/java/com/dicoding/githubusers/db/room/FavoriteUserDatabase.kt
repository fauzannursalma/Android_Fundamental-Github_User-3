package com.dicoding.githubusers.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.githubusers.db.data.FavoriteUser


@Database(
    entities = [FavoriteUser::class],
    version = 2,
    exportSchema = true
)
abstract class FavoriteUserDatabase : RoomDatabase() {
    companion object {
        var INSTANCE: FavoriteUserDatabase? = null

        fun getDatabase(context: Context): FavoriteUserDatabase? {
            if (INSTANCE == null) {
                synchronized(FavoriteUserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUserDatabase::class.java,
                        "user_database"
                    ).build()

                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteUserDao(): FavoriteUserDao
}