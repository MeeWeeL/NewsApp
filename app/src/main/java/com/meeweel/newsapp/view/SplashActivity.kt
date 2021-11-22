package com.meeweel.newsapp.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.SplashScreenLayoutBinding

class SplashActivity : AppCompatActivity() {

    private var handler = Handler()

    lateinit var bind: SplashScreenLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = SplashScreenLayoutBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.splashImage.setImageResource(R.drawable.default_picture)

        bind.splashImage
            .animate()
            .translationX(1000f)
            .setDuration(2000)
        //    .setInterpolator(BounceInterpolator())
            .start()

        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, NewsTapeActivity::class.java))
            finish()
        }, 2000)
    }
}