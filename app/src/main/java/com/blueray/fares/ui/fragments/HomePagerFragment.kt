package com.blueray.fares.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.blueray.fares.R
import com.blueray.fares.adapters.HomePagerAdapter
import com.blueray.fares.databinding.FragmentHomePagerBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomePagerFragment : Fragment() {

    private lateinit var binding : FragmentHomePagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomePagerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPagerWithTapLayout()

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Handle page change. You can notify fragments here.
                notifyFragmentsAboutPageChange(position)

            }
        })

    }
    fun notifyFragmentsAboutPageChange(selectedPosition: Int) {
        val adapter = binding.viewPager.adapter as HomePagerAdapter
        for (i in 0 until adapter.itemCount) {
            val fragmentTag = "f${binding.viewPager.id}:$i" // Correctly form the fragment tag
            val fragment = childFragmentManager.findFragmentByTag(fragmentTag)
            if (fragment is HomeVidFrag) {
                if (i == selectedPosition) {
//                    fragment.onVisibleInPager()
                    fragment.onHiddenInPager()

                } else {
                    fragment.onHiddenInPager()
                }
            }
        }
    }



    private fun setUpViewPagerWithTapLayout() {
        val adapter = HomePagerAdapter(childFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()

        val list = listOf("مقاطع","لايف")

        for (i in list.indices) {
            val item = list[i]

            // name to the tab layout
            tabListTitle.add(item)
        }

        binding.viewPager.adapter = adapter

        for (item in tabListTitle) {
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