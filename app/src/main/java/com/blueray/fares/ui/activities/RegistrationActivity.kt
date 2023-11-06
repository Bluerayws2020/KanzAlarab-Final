package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityRegisterationBinding

class RegistrationActivity : AppCompatActivity() {
    
    private lateinit var binding : ActivityRegisterationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.nextBtn.setOnClickListener { 
            startActivity(Intent(this,SecondRegistrationActivity::class.java))
        }
        
    }
}