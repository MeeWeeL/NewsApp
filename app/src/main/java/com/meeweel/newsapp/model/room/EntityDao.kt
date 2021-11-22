package com.meeweel.newsapp.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EntityDao {

    @Query("SELECT * FROM Entity")
    fun getNewsList(): List<Entity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(Entity: Entity)
}