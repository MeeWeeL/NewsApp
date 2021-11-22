package com.meeweel.newsapp.model

import com.meeweel.newsapp.model.data.NewsPost

sealed class NewsAppState {
    data class Success(val newsData: List<NewsPost>) : NewsAppState()
    class Error(val error: Throwable) : NewsAppState()
    object Loading : NewsAppState()
}