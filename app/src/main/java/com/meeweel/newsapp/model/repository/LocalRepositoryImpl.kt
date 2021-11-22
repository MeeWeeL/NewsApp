package com.meeweel.newsapp.model.repository

import com.meeweel.newsapp.model.data.NewsPicture
import com.meeweel.newsapp.model.data.NewsPost
import com.meeweel.newsapp.model.room.EntityDao
import com.meeweel.newsapp.model.room.convertEntityToNewsList
import com.meeweel.newsapp.model.room.convertEntityToPicturesList
import com.meeweel.newsapp.model.room.convertNewsPostToEntity

class LocalRepositoryImpl(
    private val localEntityDataSource: EntityDao
) : LocalRepository {

    override fun getLocalNewsList(): List<NewsPost> {
        return convertEntityToNewsList(localEntityDataSource.getNewsList())
    }

    override fun getLocalPicturesList(): List<NewsPicture> {
        return convertEntityToPicturesList(localEntityDataSource.getNewsList())
    }

    override fun insert(newsPost: NewsPost) {
        localEntityDataSource.insert(convertNewsPostToEntity(newsPost))
    }
}