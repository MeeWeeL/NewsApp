package com.meeweel.newsapp.view.gallery

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.GalleryDetailsFragmentLayoutBinding
import com.meeweel.newsapp.model.data.NewsPicture
import java.io.IOException
import java.net.URL

class GalleryDetailsFragment : Fragment() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var _binding: GalleryDetailsFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GalleryDetailsFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<NewsPicture>(BUNDLE_EXTRA)?.let { picture ->
            populateData(picture)
        }
    }

    private fun populateData(newsData: NewsPicture) {
        with(binding) {
            root.setImageResource(R.drawable.default_picture)
            loadPicture(newsData.image)
        }
    }

    private fun loadPicture(image: String) {
        Thread {
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                Thread.sleep(300)
                runOnUiThread {
                    binding.root.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    secondTryOfLoadingPicture(image)  // Повторная попытка подгрузить картинку по ссылке
                }
            }
        }.start()
    }

    private fun secondTryOfLoadingPicture(image: String) {
        Thread {
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                Thread.sleep(1000)
                runOnUiThread {
                    binding.root.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    binding.root.setImageResource(R.drawable.error_picture)
                }
            }
        }.start()
    }

    private fun runOnUiThread(r: Runnable) {
        handler.post(r) // Запуск в главном потоке
    }

    companion object {
        const val BUNDLE_EXTRA = "newsPicture"

        fun newInstance(bundle: Bundle): GalleryDetailsFragment {
            val fragment = GalleryDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}