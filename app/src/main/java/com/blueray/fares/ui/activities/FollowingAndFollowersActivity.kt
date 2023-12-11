package com.blueray.fares.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.adapters.FollowersPagerAdapter
import com.blueray.fares.adapters.HomePagerAdapter
import com.blueray.fares.databinding.ActivityFollowingAndFollowersBinding
import com.blueray.fares.helpers.HelperUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FollowingAndFollowersActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFollowingAndFollowersBinding
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingAndFollowersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userId = intent.getStringExtra("user_id") // Retrieve the user ID
        val userName = intent.getStringExtra("userName") // Retrieve the user ID
if (HelperUtils.getUid(this) == userId){
    binding.includeTab.title.text = "حسابي"
}else {
    binding.includeTab.title.text = "@$userName"

}

        setUpViewPagerWithTapLayout()
    }

    private fun setUpViewPagerWithTapLayout() {
        val adapter = FollowersPagerAdapter(supportFragmentManager, lifecycle,userId)
        val tabListTitle: MutableList<String> = ArrayList()

        val list = listOf("متابعون","يتابعون")

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