package com.example.newsify.data.remote

import com.example.newsify.data.User
import com.example.newsify.data.local.UserDataBase
import javax.inject.Inject

class MainRepository @Inject constructor(val db: UserDataBase) {
    suspend fun getAllUser() = db.userDao().getAllUserData()
    suspend fun getUser(mobileNumber: String) = db.userDao().getSelectedUserData(mobileNumber)
    suspend fun getValidUser(mobileNumber: String, password: String) =
        db.userDao().getValidUser(mobileNumber, password)

    suspend fun addUser(
        name: String,
        mobileNumber: String,
        password: String,
        gender: String,
        isLogin: Boolean
    ) = db.userDao().insertAll(
        User(
            name = name,
            mobile_number = mobileNumber,
            password = password,
            gender = gender,
            isLogin = isLogin
        )
    )

    suspend fun updateUser(user: User) {
        db.userDao().update(user)
    }
}