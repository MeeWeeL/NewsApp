package com.meeweel.newsapp.view.newstape

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.NewsRecyclerItemBinding
import com.meeweel.newsapp.model.data.NewsPost
import java.io.IOException
import java.net.URL

class NewsTapeFragmentAdapter :
    RecyclerView.Adapter<NewsTapeFragmentAdapter.MainViewHolder>() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var newsData: MutableList<NewsPost> = mutableListOf()
    private var onItemViewClickListener: NewsTapeFragment.OnItemViewClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = NewsRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(newsData[position])
    }

    override fun getItemCount(): Int {
        return newsData.size
    }

    inner class MainViewHolder(private val binding: NewsRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsPost: NewsPost) {
            binding.apply {
                newsRecyclerImage.setImageResource(R.drawable.default_picture)
                loadPicture(newsPost.image, binding)
                newsRecyclerTitle.text = newsPost.title
                newsRecyclerDate.text = newsPost.published_at
                root.setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(newsPost)
                }
            }
        }
    }

    private fun loadPicture(image: String, binding: NewsRecyclerItemBinding) {
        Thread {
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                Thread.sleep(300)
                runOnUiThread {
                    binding.newsRecyclerImage.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    secondTryOfLoadingPicture(image, binding)  // Повторная попытка подгрузить картинку по ссылке
                }
            }
        }.start()
    }

    private fun secondTryOfLoadingPicture(image: String, binding: NewsRecyclerItemBinding) {
        Thread {
            Thread.sleep(1300)
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                runOnUiThread {
                    binding.newsRecyclerImage.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    binding.newsRecyclerImage.setImageResource(R.drawable.error_picture)
                }
            }
        }.start()
    }
    private fun runOnUiThread(r: Runnable) {
        handler.post(r) // Запуск в главном потоке
    }

    fun setOnItemViewClickListener(onItemViewClickListener: NewsTapeFragment.OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }

    fun removeOnItemViewClickListener() {
        onItemViewClickListener = null
    }

    fun setNewsData(data: List<NewsPost>) {
        newsData = data.toMutableList()
        notifyDataSetChanged()
    }

}