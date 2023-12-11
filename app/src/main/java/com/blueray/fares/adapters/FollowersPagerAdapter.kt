package com.blueray.fares.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.fares.ui.fragments.FollowersFragment
import com.blueray.fares.ui.fragments.VideoListFragment

class FollowersPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle,
                             private val userId: String?
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount()= 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersFragment()

        fragment.arguments = Bundle().apply {
            putString("tab_position", position.toString())
            putString("user_id", userId)

        }
            return fragment
    }
}