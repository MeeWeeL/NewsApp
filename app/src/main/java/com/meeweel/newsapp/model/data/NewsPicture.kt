package com.meeweel.newsapp.model.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsPicture(
    val image: String
) : Parcelable