package com.meeweel.newsapp.model.room

import com.meeweel.newsapp.model.api.NewsItemResponse
import com.meeweel.newsapp.model.data.NewsPicture
import com.meeweel.newsapp.model.data.NewsPost

fun convertEntityToNewsList(entityList: List<Entity>): List<NewsPost> {
    return entityList.map {
        NewsPost(it.author, it.title, it.description, it.url, it.source, it.image, it.category,
            it.language, it.country, it.published_at
        )
    }
}

fun convertEntityToPicturesList(entityList: List<Entity>): List<NewsPicture> {
    return entityList.map {
        NewsPicture(it.image
        )
    }
}

fun convertNewsPostToEntity(post: NewsPost): Entity {
    return Entity(
        post.author,
        post.title,
        post.description,
        post.url,
        post.source,
        post.image,
        post.category,
        post.language,
        post.country,
        post.published_at
    )
}

fun convertNewsItemResponseToNewsPost(post: NewsItemResponse): NewsPost {
    return NewsPost(
        post.author ?: "Unknown",
        post.title,
        post.description,
        post.url,
        post.source,
        post.image ?: "https://uaychaiblog.files.wordpress.com/2015/08/null-1.png?w=800",
        post.category,
        post.language,
        post.country,
        post.published_at
    )
}