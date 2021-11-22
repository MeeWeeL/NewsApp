package com.meeweel.newsapp.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Entity(

    val author: String,
    val title: String,
    val description: String,
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val source: String,
    val image: String,
    val category: String,
    val language: String,
    val country: String,
    val published_at: String
)
