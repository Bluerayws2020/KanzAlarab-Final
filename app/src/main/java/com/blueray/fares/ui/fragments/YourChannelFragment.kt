package com.blueray.fares.ui.fragments

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.blueray.fares.R
import com.blueray.fares.adapters.ProfilePagerAdapter
import com.blueray.fares.databinding.FragmentYourChannelBinding
import com.blueray.fares.databinding.TabLayoutTabItmBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NewAppendItItems
import com.blueray.fares.ui.activities.Profile
import com.blueray.fares.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList


class YourChannelFragment : Fragment() {

    private lateinit var binding : FragmentYourChannelBinding
    private val mainViewModel by viewModels<AppViewModel>()
    private lateinit var navController: NavController

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

        navController = Navigation.findNavController(view)

        setUpViewPagerWithTapLayout()
getUserProifle()
        mainViewModel.retriveViewUserProfile()


        binding.textView.setOnClickListener {
        startActivity(Intent( requireContext(),Profile::class.java))

        }


    }
    
    private fun setUpViewPagerWithTapLayout() {

        val list = arrayListOf("الاحصائيات","المحتوى","المفضلة")
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

//        selectItem()
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                (tab.customView as CardView).background.setTint(ContextCompat.getColor(requireContext(),R.color.lightGrey))
                (tab.customView as CardView).findViewById<TextView>(R.id.title).setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
                ((tab.customView as CardView).findViewById(R.id.icon) as ImageView).setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), PorterDuff.Mode.SRC_IN)



            }


            override fun onTabUnselected(tab: TabLayout.Tab) {
                (tab.customView as CardView).background.setTint(ContextCompat.getColor(requireContext(),R.color.white))
                (tab.customView as CardView).findViewById<TextView>(R.id.title).setTextColor(ContextCompat.getColor(requireContext(),R.color.brown))
                ((tab.customView as CardView).findViewById(R.id.icon) as ImageView).setColorFilter(ContextCompat.getColor(requireContext(), R.color.brown), PorterDuff.Mode.SRC_IN) }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        binding.tabLayout.visibility = View.VISIBLE

        val pos = 1

        binding.viewPager.setCurrentItem(pos, false)

    }

//    private fun selectItem() {
////        get Selected Tap
//        val tab = binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)
//        (tab?.customView as CardView).background.setTint(
//            ContextCompat.getColor(
//                requireContext(),
//                R.color.lightGrey
//            )
//        )
//        (tab.customView as CardView).findViewById<TextView>(R.id.title)
//            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
//    }

    fun getUserProifle(){

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        mainViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if(result.data.isEmpty())
                    {}
                    else{
                        val  data = result.data[0]


                        Glide.with(requireContext()).load(result.data[0].user_picture).into(binding.profileImage)

                        binding.yourChannelTv.text  =  data.username

                    }

                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
                else -> {}
            }
        }

    }



}