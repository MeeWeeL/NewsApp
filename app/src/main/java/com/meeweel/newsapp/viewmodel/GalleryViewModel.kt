package com.meeweel.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meeweel.newsapp.model.GalleryAppState
import com.meeweel.newsapp.model.app.NewsApp
import com.meeweel.newsapp.model.repository.LocalRepository
import com.meeweel.newsapp.model.repository.LocalRepositoryImpl

class GalleryViewModel(private val repository: LocalRepository = LocalRepositoryImpl(NewsApp.getEntityDao())) :
    ViewModel() {

    private val liveDataToObserve: MutableLiveData<GalleryAppState> = MutableLiveData()
    fun getData(): LiveData<GalleryAppState> {
        return liveDataToObserve
    }

    fun getPicturesFromLocalSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = GalleryAppState.Loading
        Thread {
            Thread.sleep(100)
            liveDataToObserve.postValue(
                GalleryAppState.Success(
                    repository.getLocalPicturesList()
                )
            )
        }.start()
    }
}