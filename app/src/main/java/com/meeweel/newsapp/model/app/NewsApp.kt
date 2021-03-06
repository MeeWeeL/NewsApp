package com.meeweel.newsapp.model.app

import android.annotation.SuppressLint
import android.app.Application
import androidx.room.Room
import com.meeweel.newsapp.model.api.NewsApiRequests
import com.meeweel.newsapp.model.room.EntityDao
import com.meeweel.newsapp.model.room.EntityDataBase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewsApp : Application() {
    lateinit var newsApi: NewsApiRequests
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        configureRetrofit()
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.mediastack.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        newsApi = retrofit.create(NewsApiRequests::class.java)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var appInstance: NewsApp? = null
        private var dbEntity: EntityDataBase? = null
        private const val DB_WATCHED = "Repository.db"

        fun getEntityDao(): EntityDao {
            if (dbEntity == null) {
                synchronized(EntityDataBase::class.java) {
                    if (dbEntity == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        dbEntity = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            EntityDataBase::class.java,
                            DB_WATCHED
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }

            return dbEntity!!.entityDao()
        }
    }
}