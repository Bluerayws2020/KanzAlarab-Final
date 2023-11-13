package com.blueray.fares.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.blueray.fares.R
import com.blueray.fares.adapters.ProfilePagerAdapter
import com.blueray.fares.databinding.FragmentYourChannelBinding
import com.blueray.fares.databinding.TabLayoutTabItmBinding
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

        val list = arrayListOf("Analytics","Content","Dashboard")
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
            // set custom layout for all the tab layout and st the text
            val customLayoutBinding = TabLayoutTabItmBinding.inflate(layoutInflater)
            tab.customView = customLayoutBinding.root
            (tab.customView as CardView).findViewById<TextView>(R.id.title).text = tabListTitle[position]
        }.attach()

        selectItem()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                (tab.customView as CardView).background.setTint(ContextCompat.getColor(requireContext(),R.color.lightGrey))
                (tab.customView as CardView).findViewById<TextView>(R.id.title).setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                ((tab.customView as CardView).findViewById(R.id.icon) as ImageView).setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), PorterDuff.Mode.SRC_IN)



            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                (tab.customView as CardView).background.setTint(ContextCompat.getColor(requireContext(),R.color.white))
                (tab.customView as CardView).findViewById<TextView>(R.id.title).setTextColor(ContextCompat.getColor(requireContext(),R.color.brown))
                ((tab.customView as CardView).findViewById(R.id.icon) as ImageView).setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown), PorterDuff.Mode.SRC_IN)

            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding.tabLayout.visibility = View.VISIBLE

        val pos = 1

        binding.viewPager.setCurrentItem(pos, false)

    }

    private fun selectItem() {
//        get Selected Tap
        val tab = binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)
        (tab?.customView as CardView).background.setTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.lightGrey
            )
        )
        (tab.customView as CardView).findViewById<TextView>(R.id.title)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }


}