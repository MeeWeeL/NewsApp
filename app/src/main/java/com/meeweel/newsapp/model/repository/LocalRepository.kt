package com.meeweel.newsapp.model.repository

import com.meeweel.newsapp.model.data.NewsPicture
import com.meeweel.newsapp.model.data.NewsPost

interface LocalRepository {
    fun getLocalNewsList(): List<NewsPost>
    fun getLocalPicturesList(): List<NewsPicture>
    fun insert(newsPost: NewsPost)
}