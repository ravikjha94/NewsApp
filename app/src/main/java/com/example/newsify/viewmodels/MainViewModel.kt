package com.example.newsify.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsify.data.remote.MainRepository
import com.example.newsify.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (val app:Application,val mainRepository: MainRepository) : AndroidViewModel(app) {

    private val _isUserExists = MutableLiveData<Boolean>()
    val isUserExists: LiveData<Boolean>
        get() = _isUserExists

    fun checkUser(localUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val user: User = mainRepository.getUser(localUser.mobile_number)
            if (user == null){
                addUserData(localUser)
                _isUserExists.postValue(false)
            } else {
                if (user.mobile_number == localUser.mobile_number) {
                    _isUserExists.postValue(true)
                } else {
                    addUserData(localUser)
                    _isUserExists.postValue(false)
                }
            }

        }
    }

    private val _isValidUserExists = MutableLiveData<Boolean>()
    val isValidUserExists: LiveData<Boolean>
        get() = _isValidUserExists

    fun checkValidUser(localUser: User){
        viewModelScope.launch(Dispatchers.IO) {
            val user: User = mainRepository.getValidUser(localUser.mobile_number,localUser.password,)
            if (user == null){
                _isValidUserExists.postValue(false)
            } else {
                if (user.mobile_number == localUser.mobile_number && user.password == localUser.password) {
                      addUserData(localUser)
                    _isValidUserExists.postValue(true)
                } else {
                    _isValidUserExists.postValue(false)
                }
            }

        }
    }

     private fun addUserData(user: User){
        viewModelScope.launch {
            mainRepository.addUser(
                name = user.name,
                mobileNumber = user.mobile_number,
                password = user.password,
                gender = user.gender,
                isLogin= true
            )
        }
        }
    private val _isLoggedIn = MutableLiveData<Boolean>()
   val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn
    private val _lastUser = MutableLiveData<User>()
    val lastUser: LiveData<User>
        get() = _lastUser

    fun getAllUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val user=mainRepository.getAllUser()
            if (user!=null){
                if(user.isLogin){
                    _isLoggedIn.postValue(true)
                      _lastUser.postValue(user)
                }else{
                    _isLoggedIn.postValue(false)
                }
            }else{
                _isLoggedIn.postValue(false)
            }
        }
    }
    fun setLogoutForUser(user: User){
        viewModelScope.launch{
            mainRepository.addUser( name = user.name,
                mobileNumber = user.mobile_number,
                password = user.password,
                gender = user.gender,
                isLogin= false)
        }

    }

}
