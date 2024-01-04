package com.blueray.fares.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.blueray.fares.R
import com.blueray.fares.adapters.MyAccountPagerAdapter
import com.blueray.fares.databinding.FragmentMyAccountBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.activities.FollowingAndFollowersActivity
import com.blueray.fares.ui.activities.Profile
import com.blueray.fares.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MyAccountFragment : Fragment() {

    private lateinit var binding: FragmentMyAccountBinding
    private val mainViewModel by viewModels<AppViewModel>()
var userName = ""
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
        mainViewModel.retriveViewUserProfile()
        getUserProifle()

        binding.settings.setOnClickListener {
            startActivity(Intent(requireContext(),Profile::class.java))
        }
        binding.followersLayout.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
           intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID

            startActivity(intent)


        }


        binding.followingCount.setOnClickListener {
            val intent  = Intent(requireContext(), FollowingAndFollowersActivity::class.java)
            intent.putExtra("user_id", HelperUtils.getUid(requireContext())) // Replace 'yourUserId' with the actual user ID
            intent.putExtra("userName",userName ) // Replace 'yourUserId' with the actual user ID

            startActivity(intent)
        }
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
    fun getUserProifle(){


        mainViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    val  data = result.data[0]
                    Glide.with(this).load(result.data[0].user_picture).placeholder(R.drawable.logo2).into(binding.profileImage)
                  binding.followersCount.text =  data.autherFoloower.numOfFollowers.toString()
                    binding.followingCount.text =  data.autherFoloower.numOfFollowing.toString()
                    binding.likesCount.text =  data.autherFoloower.numOfLikes.toString()

                    binding.userName.text = data.username.toString()

                    userName =  data.username


                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
            }
        }

    }



}