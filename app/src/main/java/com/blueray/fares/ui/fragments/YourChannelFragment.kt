package com.blueray.fares.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blueray.fares.R
import com.blueray.fares.adapters.ProfilePagerAdapter
import com.blueray.fares.databinding.FragmentYourChannelBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList


class YourChannelFragment : Fragment() {

    private lateinit var binding : FragmentYourChannelBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentYourChannelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPagerWithTapLayout()
    }
    
    private fun setUpViewPagerWithTapLayout() {

        val list = arrayListOf("1","2","3")
        val adapter = ProfilePagerAdapter(parentFragmentManager,lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()
        list.forEach {
            adapter.addProductsList(list)
            tabListTitle.add(it)
        }
        binding.viewPager.adapter = adapter

        for (item in tabListTitle){
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(item))
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabListTitle[position]
        }.attach()

    }


}