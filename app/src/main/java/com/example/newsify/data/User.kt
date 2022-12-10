package com.example.newsify.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    @ColumnInfo(name = "user_name")
    val name:String="",
    @ColumnInfo(name = "user_mobile_number")
    val mobile_number:String="",
    @ColumnInfo(name = "user_password")
    val password:String="",
    val gender:String="",
    @ColumnInfo(name="isLogin")
    val isLogin:Boolean=false

)
