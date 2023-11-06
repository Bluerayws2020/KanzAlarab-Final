package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityThirdRegistrationBinding

class ThirdRegistrationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityThirdRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this,Profile::class.java))
        }

    }
}