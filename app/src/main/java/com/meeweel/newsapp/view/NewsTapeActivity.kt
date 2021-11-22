package com.meeweel.newsapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meeweel.newsapp.R
import com.meeweel.newsapp.view.newstape.NewsTapeFragment

class NewsTapeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_layout)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewsTapeFragment.newInstance())
                .commitNow()
        }
    }
}