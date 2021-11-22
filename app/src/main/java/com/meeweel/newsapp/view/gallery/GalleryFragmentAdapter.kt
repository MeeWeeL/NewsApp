package com.meeweel.newsapp.view.gallery

import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.GalleryRecyclerItemBinding
import com.meeweel.newsapp.model.data.NewsPicture
import java.io.IOException
import java.net.URL

class GalleryFragmentAdapter :
    RecyclerView.Adapter<GalleryFragmentAdapter.MainViewHolder>() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var galleryData: List<NewsPicture> = mutableListOf()
    private var onItemViewClickListener: GalleryFragment.OnItemViewClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = GalleryRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(galleryData[position])
    }

    override fun getItemCount(): Int {
        return galleryData.size
    }

    inner class MainViewHolder(private val binding: GalleryRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(newsPicture: NewsPicture) {
            binding.apply {
                galleryRecyclerImage.setImageResource(R.drawable.default_picture)
                loadPicture(newsPicture.image, binding)
                root.setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(newsPicture)
                }
            }
        }
    }


    private fun loadPicture(image: String, binding: GalleryRecyclerItemBinding) {
        Thread {
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                Thread.sleep(300)
                runOnUiThread {
                    binding.galleryRecyclerImage.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    secondTryOfLoadingPicture(image, binding)  // Повторная попытка подгрузить картинку по ссылке
                }
            }
        }.start()
    }

    private fun secondTryOfLoadingPicture(image: String, binding: GalleryRecyclerItemBinding) {
        Thread {
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                Thread.sleep(800)
                runOnUiThread {
                    binding.galleryRecyclerImage.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    binding.galleryRecyclerImage.setImageResource(R.drawable.error_picture)
                }
            }
        }.start()
    }

    private fun runOnUiThread(r: Runnable) {
        handler.post(r) // Запуск в главном потоке
    }

    fun setOnItemViewClickListener(onItemViewClickListener: GalleryFragment.OnItemViewClickListener) {
        this.onItemViewClickListener = onItemViewClickListener
    }

    fun removeOnItemViewClickListener() {
        onItemViewClickListener = null
    }

    fun setGalleryData(data: List<NewsPicture>) {
        galleryData = data
        notifyDataSetChanged()
    }

}