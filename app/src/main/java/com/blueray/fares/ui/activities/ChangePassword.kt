package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivitiesCountItemsBinding
import com.blueray.fares.databinding.ActivityChangePasswordBinding

class ChangePassword : BaseActivity() {
    private lateinit var binding : ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.nextBtn.setOnClickListener { 
            startActivity(Intent(this,HomeActivity::class.java))
        }
        
    }
}