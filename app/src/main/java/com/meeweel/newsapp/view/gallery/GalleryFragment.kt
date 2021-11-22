package com.meeweel.newsapp.view.gallery

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.meeweel.newsapp.R
import com.meeweel.newsapp.databinding.GalleryFragmentLayoutBinding
import com.meeweel.newsapp.model.GalleryAppState
import com.meeweel.newsapp.model.data.NewsPicture
import com.meeweel.newsapp.view.newstape.NewsTapeFragment
import com.meeweel.newsapp.viewmodel.GalleryViewModel

class GalleryFragment : Fragment() {

    private val viewModel: GalleryViewModel by lazy {
        ViewModelProvider(this).get(GalleryViewModel::class.java)
    }
    private var _binding: GalleryFragmentLayoutBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = GalleryFragmentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GalleryFragmentLayoutBinding.inflate(inflater, container, false)
        binding.navBar.background = null
        binding.navBar.menu.findItem(R.id.gallery_nav).isChecked = true
        binding.navBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.news_nav -> requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, NewsTapeFragment.newInstance())
                    .commitNow()
            }
            true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.galleryRecycler.layoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) // Для поддержки ориентации
                GridLayoutManager(requireContext(), 5)
            else GridLayoutManager(requireContext(), 7)
        adapter.setOnItemViewClickListener(object : OnItemViewClickListener {
            override fun onItemViewClick(newsPicture: NewsPicture) {
                activity?.supportFragmentManager?.apply {
                    beginTransaction()
                        .replace(R.id.container, GalleryDetailsFragment.newInstance(Bundle().apply {
                            putParcelable(GalleryDetailsFragment.BUNDLE_EXTRA, newsPicture)
                        }))
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }
        })

        binding.galleryRecycler.adapter = adapter

        val observer = Observer<GalleryAppState> { a ->
            renderData(a)
        }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getPicturesFromLocalSource()
    }


    private fun renderData(data: GalleryAppState) = when (data) {
        is GalleryAppState.Success -> {
            val galleryData = data.newsData
            binding.loadingLayout.visibility = View.GONE
            adapter.setGalleryData(galleryData)
        }
        is GalleryAppState.Loading -> {
            binding.loadingLayout.visibility = View.VISIBLE
        }
        is GalleryAppState.Error -> {
            binding.loadingLayout.visibility = View.GONE

        }
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(newsPicture: NewsPicture)
    }

    companion object {
        fun newInstance() = GalleryFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.removeOnItemViewClickListener()
    }
}