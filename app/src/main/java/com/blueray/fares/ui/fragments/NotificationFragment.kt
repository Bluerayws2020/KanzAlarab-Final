package com.blueray.fares.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.NotficationAcdapter
import com.blueray.fares.databinding.FragmentNotficationBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.ui.activities.MainActivity
import com.blueray.fares.ui.viewModels.AppViewModel



class NotificationFragment : Fragment() {
    private val viewmodel by viewModels<AppViewModel>()

    private lateinit var binding : FragmentNotficationBinding
    private lateinit var notfiAdabter : NotficationAcdapter
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNotficationBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        notfiAdabter = NotficationAcdapter(arrayListOf("test1","test2"))
                            binding.rvNotifications.adapter = notfiAdabter
                            binding.rvNotifications.layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.VERTICAL, true
                            )// trying reversed layout
        binding.includeTap.profile.setOnClickListener {

            if (HelperUtils.getUid(requireContext()) == "0"){
                Toast.makeText(context,"يجب تسجيل الدخول",Toast.LENGTH_LONG).show()

                startActivity(Intent(requireContext(), MainActivity::class.java))

                false
            }else {
                navController.navigate(R.id.yourChannelFragment)

            }


//
        }



//        getData()
        return binding.root
    }
    // setting up action bar
//    private fun setUpActionBar() {
//        binding.includedTap.back.hide()
////            .setOnClickListener {
////            (activity as HomeActivity).onBackPressedDispatcher.onBackPressed()
////        }
//        binding.includedTap.title.text =getString(R.string.notifications)
//        binding.includedTap.menu.setOnClickListener {
//            (activity as HomeActivity).openDrawer()
//        }

//    }

//    fun getData(){
//        viewmodel.getMuNotfi().observe(viewLifecycleOwner) { result ->
//
//binding.progressBar.hide()
//            when (result) {
//                is NetworkResults.Success -> {
//
//                    if (result.data.status.status == 200) {
//
//
//                        if (result.data.data.isNullOrEmpty()){
//                            binding.messageSearch.show()
//
//                        }else {
//                            binding.messageSearch.hide()
//
//                            notfiAdabter = NotficationAcdapter(result.data.data)
//                            binding.rvNotifications.adapter = notfiAdabter
//                            binding.rvNotifications.layoutManager = LinearLayoutManager(
//                                requireContext(),
//                                LinearLayoutManager.VERTICAL, true
//                            )// trying reversed layout
//
//                        }
//
//
//
//                    } else {
////                        binding.messageSearch.show()
//                    }
//                }
//                is NetworkResults.Error -> {
//                    result.exception.printStackTrace()
//                }
//
//                else -> {}
//            }
//        }
//    }
//    fun getDeleteAllIteem(){
//
//
//        viewmodel.getdeletAllNotfi().observe(viewLifecycleOwner) { result ->
//binding.progressBar.hide()
//            when (result) {
//                is NetworkResults.Success -> {
//
//                    if(result.data.status.status == 200){
//                        showMessage(result.data.status.msg .toString())
//                        viewmodel.retriveMyNotfi()
//
//
//
//                    }else {
////                        binding.bottomLayout.hide()
//
//                        showMessage(result.data.status.msg .toString())
//                    }
//                }
//                is NetworkResults.Error -> {
//                    result.exception.printStackTrace()
//                    showMessage(getString(R.string.error))
//                }
//
//                is NetworkResults.PartialSuccess -> TODO()
//                else -> {}
//            }
//        }
//    }
    private fun showMessage(message: String?) = Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()


}