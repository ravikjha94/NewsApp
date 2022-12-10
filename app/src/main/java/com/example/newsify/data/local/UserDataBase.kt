package com.example.newsify.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsify.data.User

@Database(entities = [User::class], version = 2)
abstract class UserDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object{
        @Volatile
        private var INSTANCE: UserDataBase?=null
        fun getInstance(context: Context): UserDataBase {
            synchronized(this){
                var instance= INSTANCE
                if(instance == null){
                    instance= Room.databaseBuilder(context.applicationContext,
                        UserDataBase::class.java,"user_table")
                        .build()
                    INSTANCE =instance
                }
                return instance
            }

        }
    }
}