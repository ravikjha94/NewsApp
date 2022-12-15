package com.example.newsify.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsify.constant.Constant
import com.example.newsify.data.remote.MainRepository
import com.example.newsify.data.User
import com.example.newsify.network.NewApiInterface
import com.example.newsify.ui.data.NewsResponseDataClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (val app:Application,val mainRepository: MainRepository) : AndroidViewModel(app) {

    private val _isUserExists = MutableLiveData<Boolean>()
    val isUserExists: LiveData<Boolean>
        get() = _isUserExists

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String>
        get() = _showToast

    private val _response = MutableLiveData<NewsResponseDataClass>()
    val response: LiveData<NewsResponseDataClass>
        get() = _response
    @Inject
    lateinit var retrofit: Retrofit

    fun checkUser(localUser: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val user: User = mainRepository.getUser(localUser.mobile_number?: "")
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
            val user: User = mainRepository.getValidUser(localUser.mobile_number?: "",localUser.password?: "",)
            if (user == null){
                _isValidUserExists.postValue(false)
            } else {
                if (user.mobile_number == localUser.mobile_number && user.password == localUser.password) {
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
                name = user.name ?: "",
                mobileNumber = user.mobile_number?: "",
                password = user.password?: "",
                gender = user.gender?: "",
                isLogin= true
            )
        }
        }
    private val _lastUser = MutableLiveData<List<User>>()
    val lastUser: LiveData<List<User>>
        get() = _lastUser

    fun getAllUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val user=mainRepository.getAllUser()
            if (user!=null){
                if(user.isNotEmpty()){
                      _lastUser.postValue(user)
                }else{
                    _lastUser.postValue(emptyList())
                }
            }else{
                _lastUser.postValue(emptyList())
            }
        }
    }
    fun setLogoutForUser(user: User){
        viewModelScope.launch{
            user.isLogin = false
            mainRepository.updateUser(user)
        }

    }

    fun setLogInForUser(user: User){
        viewModelScope.launch{
            user.isLogin = true
            mainRepository.updateUser(user)
        }

    }

    fun getNewsDeatils(query: String) {
        val services: NewApiInterface =retrofit.create(NewApiInterface::class.java)
        val listCall: Call<NewsResponseDataClass> = services.getNews(query=query,apikey=Constant.API_KEY)
        listCall.enqueue(object : Callback<NewsResponseDataClass> {
            override fun onResponse(
                call: Call<NewsResponseDataClass>,
                response: Response<NewsResponseDataClass>
            ) {
                if (response.isSuccessful){
                    _response.value = response.body()
                }else{
                    val rc=response.code()
                    when(rc){
                        400->{
                            _showToast.value = "Bad Connection"
                        }
                        404->{
                            _showToast.value = "Data not found"
                        }
                        else->{
                            _showToast.value = "Generic error"
                        }
                    }
                }

            }

            override fun onFailure(call: Call<NewsResponseDataClass>, t: Throwable) {
                _showToast.value = "${t.message.toString()}"
            }

        })
    }

}
