package com.meeweel.newsapp.model

import com.meeweel.newsapp.model.data.NewsPicture

sealed class GalleryAppState {
    data class Success(val newsData: List<NewsPicture>) : GalleryAppState()
    class Error(val error: Throwable) : GalleryAppState()
    object Loading : GalleryAppState()
}