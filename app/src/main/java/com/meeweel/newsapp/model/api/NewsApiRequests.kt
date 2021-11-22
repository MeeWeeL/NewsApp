package com.meeweel.newsapp.model.api


import com.meeweel.newsapp.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface NewsApiRequests {

    @GET("./v1/news")
    // @Query добавляет в https запрос &date=$date
    fun getNews(@Query("access_key") apiKey: String = BuildConfig.API_KEY,
                @Query("date") date: String = "2021-${(Date().month.toInt()+1).toString()}-${(Date().date.toInt()).toString()}",
                @Query("sources") sources: String = "en"): Single<NewsResponse>
    // @Query("date") date: String = "2021-${(Date().month.toInt()+1).toString()}-${(Date().date.toInt()).toString()}",
    // Вот такое можно добавить для даты
}