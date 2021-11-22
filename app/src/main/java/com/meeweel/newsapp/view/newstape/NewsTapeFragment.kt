package com.meeweel.newsapp.view.newstape

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.NewsTapeFragmentLayoutBinding
import com.meeweel.newsapp.model.NewsAppState
import com.meeweel.newsapp.model.api.NewsResponse
import com.meeweel.newsapp.model.app.NewsApp
import com.meeweel.newsapp.model.data.NewsPost
import com.meeweel.newsapp.model.room.convertNewsItemResponseToNewsPost
import com.meeweel.newsapp.view.gallery.GalleryFragment
import com.meeweel.newsapp.viewmodel.NewsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class NewsTapeFragment : Fragment() {


    private val compositeDisposable = CompositeDisposable()
    private lateinit var handler: Handler
    private val viewModel: NewsViewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }
    private var _binding: NewsTapeFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = NewsTapeFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        handler = Handler(requireContext().mainLooper)
        _binding = NewsTapeFragmentLayoutBinding.inflate(inflater, container, false)
        binding.navBar.background = null
        binding.navBar.menu.findItem(R.id.news_nav).isChecked = true
        binding.navBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.gallery_nav -> requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, GalleryFragment.newInstance())
                    .commitNow()
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter.setOnItemViewClickListener(object : OnItemViewClickListener {
            override fun onItemViewClick(newsPost: NewsPost) {
                if (resources.getBoolean(R.bool.isLandscape)) {
                    activity?.supportFragmentManager?.apply {
                        beginTransaction()
                            .replace(R.id.news_post_container, NewsDetailsFragment.newInstance(Bundle().apply {
                                putParcelable(NewsDetailsFragment.BUNDLE_EXTRA, newsPost)
                            }))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                } else {
                    activity?.supportFragmentManager?.apply {
                        beginTransaction()
                            .replace(R.id.container, NewsDetailsFragment.newInstance(Bundle().apply {
                                putParcelable(NewsDetailsFragment.BUNDLE_EXTRA, newsPost)
                            }))
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
            }
        })

        binding.newsTapeRecycler.adapter = adapter

        val observer = Observer<NewsAppState> { a ->
            renderData(a)
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getNewsFromLocalSource()

        binding.swipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            val api = (requireActivity().application as NewsApp).newsApi
            compositeDisposable.add(
                api.getNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        toast(getString(R.string.connection_success_message))
                        saveNews(it)
                        binding.swipeRefreshLayout.isRefreshing = false
                    }, {
                        toast(getString(R.string.connection_error_message))
                        if (!isOnline()) {
                            toast("No internet connection")
                        } else {
                            it.message?.let { it1 -> longToast(it1) }
                        }
                        binding.swipeRefreshLayout.isRefreshing = false
                    })
            )
        }
    }

    private fun saveNews(news: NewsResponse) {
        for (item in news.newsResponse) {
            viewModel.insertNewsPost(convertNewsItemResponseToNewsPost(item))
        }
        viewModel.getNewsFromLocalSource()
    }

    private fun renderData(data: NewsAppState) = when (data) {
        is NewsAppState.Success -> {
            val newsData = data.newsData
            binding.loadingLayout.visibility = View.GONE
            adapter.setNewsData(newsData)
        }
        is NewsAppState.Loading -> {
            binding.loadingLayout.visibility = View.VISIBLE
        }
        is NewsAppState.Error -> {
            binding.loadingLayout.visibility = View.GONE

        }
    }

    private fun isOnline(context: Context = requireContext()): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }

    private fun toast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun longToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(newsPost: NewsPost)
    }

    companion object {
        fun newInstance() = NewsTapeFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.removeOnItemViewClickListener()
    }
}