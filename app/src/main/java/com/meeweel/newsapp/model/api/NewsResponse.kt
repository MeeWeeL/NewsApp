package com.meeweel.newsapp.model.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Header

data class NewsResponse(
    // Если переменная не совпадает с названием в гсон файле, тогда
    // необходима аннотация с указанием названия переменной в гсоне
    @SerializedName("data")
    @Header("content-type: application/json")
    val newsResponse: List<NewsItemResponse>
    )

data class NewsItemResponse(
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val source: String,
    val image: String?,
    val category: String,
    val language: String,
    val country: String,
    val published_at: String,
)