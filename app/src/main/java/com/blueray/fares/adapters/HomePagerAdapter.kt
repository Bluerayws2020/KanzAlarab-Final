package com.blueray.fares.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.fares.ui.fragments.HomeLivesFragment
import com.blueray.fares.ui.fragments.HomeVidFrag
import com.blueray.fares.ui.fragments.VideoListFragment

class HomePagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount()= 2

    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            val videosFragment = HomeVidFrag()
            videosFragment
        } else{
            val livesFragment = HomeLivesFragment()
            livesFragment
        }
    }




}