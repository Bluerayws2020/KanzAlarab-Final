package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.blueray.fares.ui.viewModels.AppViewModel
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : AppViewModel by viewModels()
    
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.poetButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.guestBtn.setOnClickListener {
            // go to home activity
        }

    }
}