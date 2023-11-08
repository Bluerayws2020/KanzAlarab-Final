package com.blueray.fares.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.VideoItemAdapter
import com.blueray.fares.databinding.FragmentVideoListBinding
import com.google.android.material.progressindicator.BaseProgressIndicator

class VideoListFragment : Fragment() {

    private lateinit var binding : FragmentVideoListBinding
    private lateinit var adapter :VideoItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVideoListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

    }

    private fun prepareRecyclerView() {
        binding.videosRv.layoutManager = GridLayoutManager(requireContext(),3)

        adapter = VideoItemAdapter(listOf()){
            // go to video
        }

        binding.videosRv.adapter = adapter

    }

}