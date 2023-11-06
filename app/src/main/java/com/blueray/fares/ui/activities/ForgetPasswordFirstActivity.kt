package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityForgetPasswordFirstBinding

class ForgetPasswordFirstActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgetPasswordFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this,ForgetPasswordOtpActivity::class.java))
        }

    }
}