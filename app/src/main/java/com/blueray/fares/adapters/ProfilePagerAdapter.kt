package com.blueray.fares.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blueray.fares.ui.fragments.VideoListFragment

class ProfilePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    // todo change the list model
    private val productsList: MutableList<ArrayList<String>> = ArrayList()

    fun addProductsList(products: List<String>) {
        productsList.add(ArrayList(products))
    }


    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun createFragment(position: Int): Fragment {
        val productFragment = VideoListFragment()
        val bundle = Bundle()

        //Must be Parcelable
        // todo make the model parcelable

        bundle.putStringArrayList("products", productsList[position])
        productFragment.arguments = bundle
        return productFragment
    }
}