package com.meeweel.newsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meeweel.newsapp.model.NewsAppState
import com.meeweel.newsapp.model.app.NewsApp.Companion.getEntityDao
import com.meeweel.newsapp.model.data.NewsPost
import com.meeweel.newsapp.model.repository.LocalRepository
import com.meeweel.newsapp.model.repository.LocalRepositoryImpl
import java.lang.Thread.sleep

class NewsViewModel(private val repository: LocalRepository = LocalRepositoryImpl(getEntityDao())) :
    ViewModel() {

    private val liveDataToObserve: MutableLiveData<NewsAppState> = MutableLiveData()
    fun getData(): LiveData<NewsAppState> {
        return liveDataToObserve
    }

    fun getNewsFromLocalSource() = getDataFromLocalSource()

    fun insertNewsPost(post: NewsPost) {
        repository.insert(post)
    }
    private fun getDataFromLocalSource() {
        liveDataToObserve.value = NewsAppState.Loading
        Thread {
            sleep(100)
            liveDataToObserve.postValue(
                NewsAppState.Success(
                    repository.getLocalNewsList().sortedBy { it.published_at }.asReversed()
                )
            )
        }.start()
    }
}