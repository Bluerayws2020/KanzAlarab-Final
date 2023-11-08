package com.blueray.fares.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.blueray.fares.adapters.ProfilePagerAdapter
import com.blueray.fares.adapters.VideoItemAdapter
import com.blueray.fares.databinding.FragmentPartitionChannelBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList


class PartitionChannelFragment : Fragment() {

    private lateinit var binding : FragmentPartitionChannelBinding
    private lateinit var adapter :VideoItemAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPartitionChannelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRv()
    }
    
    private fun setUpRv() {
        binding.videosRv.layoutManager = GridLayoutManager(requireContext(),3)

        adapter = VideoItemAdapter(listOf()){
            // go to video
        }
        binding.videosRv.adapter = adapter
    }


}