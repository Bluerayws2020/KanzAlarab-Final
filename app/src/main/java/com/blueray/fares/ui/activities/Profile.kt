package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityMainBinding
import com.blueray.fares.databinding.ActivityProfileBinding
import com.blueray.fares.databinding.FragmentYourChannelBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide

class Profile : BaseActivity() {

    private lateinit var binding : ActivityProfileBinding
    private val mainViewModel by viewModels<AppViewModel>()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.retriveViewUserProfile()
getUserProifle()


        binding.logout.setOnClickListener {


            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)


            sharedPreferences.edit().apply {
                putString(HelperUtils.UID_KEY, "0")




            }.apply()            // go to home activity
            startActivity(Intent(this,MainActivity::class.java))


        }
    }


    fun getUserProifle(){


        mainViewModel.getUserProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    val  data = result.data[0]
                    Glide.with(this).load(result.data[0].user_picture).into(binding.profileID)
                    binding.userNameEt.setText(data.username)
                    binding.phoneNumberEt.setText(data.username)
                    binding.emailEt.setText(data.profile_data.mail)



                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
            }
        }

    }
}