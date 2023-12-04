package com.blueray.fares.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.FollowersAdapter
import com.blueray.fares.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private lateinit var binding : FragmentFollowersBinding
    private lateinit var adapter : FollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        adapter = FollowersAdapter(listOf())
        val lm  = LinearLayoutManager(requireContext())
        binding.followersRv.adapter = adapter
        binding.followersRv.layoutManager = lm
    }
}