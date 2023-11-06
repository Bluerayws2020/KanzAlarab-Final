package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
        binding.signInBtn.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
        
        binding.forgotPassword.setOnClickListener { 
            startActivity(Intent(this,ForgetPasswordFirstActivity::class.java))
        }

    }
}