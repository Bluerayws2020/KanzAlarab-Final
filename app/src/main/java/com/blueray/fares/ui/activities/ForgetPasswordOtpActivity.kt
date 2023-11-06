package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityForgetPasswordOtpBinding

class ForgetPasswordOtpActivity : AppCompatActivity() {

    private lateinit var binding : ActivityForgetPasswordOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.verify.setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }

    }
}