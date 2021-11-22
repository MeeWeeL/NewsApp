package com.meeweel.newsapp.view.customviews

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class CustomCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : CardView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) { // Размер вьюшки
        super.onMeasure(widthMeasureSpec, widthMeasureSpec) // Сделал квадратом
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas) // Визуал
    }
}