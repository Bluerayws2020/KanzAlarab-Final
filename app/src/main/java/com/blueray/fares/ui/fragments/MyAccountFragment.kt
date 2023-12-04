package com.blueray.fares.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blueray.fares.R
import com.blueray.fares.adapters.MyAccountPagerAdapter
import com.blueray.fares.databinding.FragmentMyAccountBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MyAccountFragment : Fragment() {

    private lateinit var binding: FragmentMyAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPagerWithTapLayout()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setUpViewPagerWithTapLayout() {
        val adapter = MyAccountPagerAdapter(childFragmentManager, lifecycle)
        val tabListTitle: MutableList<String> = ArrayList()

        val list = listOf("", "")

        for (i in list.indices) {
            val item = list[i]

            // name to the tab layout
            tabListTitle.add(item)
        }

        binding.viewPager.adapter = adapter

        for (i in tabListTitle.indices) {

            binding.tabLayout.addTab(binding.tabLayout.newTab())

        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.img_1)
                // set drawable bounds
                drawable?.setBounds(
                    0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                tab.icon = drawable
            } else {
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.img)
                drawable?.setBounds(
                    0, 0, drawable.intrinsicWidth,
                    drawable.intrinsicHeight
                )
                tab.icon = drawable
            }
        }.attach()
    }

}