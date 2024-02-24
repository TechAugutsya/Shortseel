package com.example.shortseel.fragment

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shortseel.R
import com.example.shortseel.model.Post
import com.example.shortseel.recyclerviewadapters.VideoListAdapter
import com.example.shortseel.viewmodels.VideoListViewModel
import com.example.shortseel.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var fragmentBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentBinding!!
    private var videoList = ArrayList<Post>()
    private lateinit var videoListModel: VideoListViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val videoFragment = VideoFragment()
        videoListModel = ViewModelProvider(requireActivity())[VideoListViewModel::class.java]

        Log.d("YT Shorts", "Called onCreate")


        val itemAdapter = VideoListAdapter(videoList)
        itemAdapter.setClickListener(object : VideoListAdapter.OnClickListener {
            override fun onClick(position: Int, model: Post) {
                videoListModel.updateVideoList(position, model)
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.flFragment, videoFragment)
                    addToBackStack("videoFragment")
                    commit()
                }
            }
        })

        videoListModel.getVideoList().observe(viewLifecycleOwner) {
            Log.d("YT Shorts", "success")
            videoList.clear()
            videoList.addAll(it)
            itemAdapter.notifyItemRangeChanged(0, it.size)
            binding.progressBar.visibility = ProgressBar.GONE
        }
        videoListModel.getNetworkError().observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = ProgressBar.GONE
                Toast.makeText(
                    requireContext(),
                    "Network error try after some time",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.recyclerView.apply {
            layoutManager =
                GridLayoutManager(context, resources.getInteger(R.integer.video_card_span))
            adapter = itemAdapter
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    modelFetch()
                }
            }
        })
        initFetch(resources.getInteger(R.integer.initialFetch))
    }

    private fun modelFetch() {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        Log.d("YT Shorts", "model fetch")
        lifecycleScope.launchWhenCreated {
            Log.d("YT Shorts", "Scope Called")
            videoListModel.fetchVideo()
        }
    }

    private fun initFetch(pageNumber: Int) {
        binding.progressBar.visibility = ProgressBar.VISIBLE
        lifecycleScope.launchWhenCreated {
            videoListModel.initialFetch(pageNumber)
        }
    }

    override fun onPause() {
        super.onPause()
        videoListModel.getVideoList().removeObservers(viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentBinding = null
    }
}