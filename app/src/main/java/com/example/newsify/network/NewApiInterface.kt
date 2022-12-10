package com.example.newsify.network

import com.example.newsify.ui.data.NewsResponseDataClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewApiInterface {
    @GET("/v2/everything")
    fun getNews(
        @Query("q") query:String,
        @Query("apikey")apikey:String,
    ): Call<NewsResponseDataClass>

}