package com.example.newsify.ui.data
import com.example.newsify.ui.data.Articles
import java.io.Serializable
data class NewsResponseDataClass(val status:String,
                                 val totalResults:Int,
                                 val articles:List<Articles>):Serializable





