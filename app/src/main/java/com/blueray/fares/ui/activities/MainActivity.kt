package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.fares.ui.viewModels.AppViewModel
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityMainBinding
import com.blueray.fares.helpers.HelperUtils
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : BaseActivity() {

    private val viewModel : AppViewModel by viewModels()

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)





//        binding.poetButton.setOnClickListener {
//            startActivity(Intent(this,LoginActivity::class.java))
//        }
//        binding.guestBtn.setOnClickListener {
//            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//
//
//            sharedPreferences.edit().apply {
//                putString(HelperUtils.UID_KEY, "0")
//
//
//
//
//            }.apply()            // go to home activity
//            startActivity(Intent(this,HomeActivity::class.java))
//
//        }


    }
}