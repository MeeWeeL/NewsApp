package com.meeweel.newsapp.view.newstape

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.NewsTapeDetailsFragmentLayoutBinding
import com.meeweel.newsapp.model.data.NewsPost
import java.io.IOException
import java.net.URL


class NewsDetailsFragment : Fragment() {

    private val handler: Handler = Handler(Looper.getMainLooper())
    private var _binding: NewsTapeDetailsFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewsTapeDetailsFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<NewsPost>(BUNDLE_EXTRA)?.let { anime ->
            populateData(anime)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun populateData(newsData: NewsPost) {
        with(binding) {
            detailsFragmentImage.setImageResource(R.drawable.default_picture)
            loadPicture(newsData.image)
            detailsFragmentDescription.text = newsData.description
            detailsFragmentTitle.text = newsData.title
            detailsFragmentDate.text = "${getString(R.string.date_)} ${newsData.published_at}"
            detailsFragmentAuthor.text = "${getString(R.string.author_)} ${newsData.author}"
            detailsFragmentCategory.text = "${getString(R.string.category_)} ${newsData.category}"
            detailsFragmentCountry.text = "${getString(R.string.country_)} ${newsData.country}"
            detailsFragmentLanguage.text = "${getString(R.string.language_)} ${newsData.language}"

            val spannableString = SpannableString(getString(R.string.link))
            val url = newsData.url
            spannableString.setSpan(
                URLSpan(url),
                0,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.design_default_color_secondary_variant
                    )
                ),
                0,
                spannableString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            detailsFragmentLink.text = spannableString
            detailsFragmentLink.movementMethod =
                LinkMovementMethod.getInstance() // Делает надпись кликабельной

            detailsFragmentSource.text = "${getString(R.string.source_)} ${newsData.source}"

        }
    }

    private fun loadPicture(image: String) {
        Thread {
            try {
                val url = URL(image)
                val bitmapPicture = BitmapFactory.decodeStream(url.openStream())
                runOnUiThread {
                    binding.detailsFragmentImage.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    secondTryOfLoadingPicture(image) // Повторная попытка подгрузить картинку по ссылке
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
                    binding.detailsFragmentImage.setImageBitmap(bitmapPicture)
                }
            } catch (e: IOException) {
                runOnUiThread {
                    binding.detailsFragmentImage.setImageResource(R.drawable.error_picture)
                }
            }
        }.start()
    }

    private fun runOnUiThread(r: Runnable) {
        handler.post(r) // Запуск в главном потоке
    }

    companion object {
        const val BUNDLE_EXTRA = "newsPost"

        fun newInstance(bundle: Bundle): NewsDetailsFragment {
            val fragment = NewsDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}