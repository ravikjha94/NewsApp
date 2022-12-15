package com.example.newsify.data.local

import androidx.room.*
import com.example.newsify.data.User

@Dao
interface UserDao {
    @Insert
   suspend fun insertAll(vararg users: User)

    @Delete
   suspend fun delete(user: User)

   @Query("SELECT * FROM user_table ")
   suspend fun getAllUserData(): List<User>

    @Query("SELECT * FROM user_table WHERE user_mobile_number = :mobile_number")
     suspend fun getSelectedUserData(mobile_number: String): User

    @Query("SELECT * FROM user_table WHERE user_mobile_number = :mobile_number" + " AND user_password = :password")
    suspend fun getValidUser(mobile_number: String,password:String): User

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User)
}