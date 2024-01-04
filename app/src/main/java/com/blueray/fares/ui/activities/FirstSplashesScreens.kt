package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.blueray.fares.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstSplashesScreens : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_splashes_screens)
        lifecycleScope.launch {
            delay(1000)
            val intent =
                   Intent(this@FirstSplashesScreens,
                    SplashScreen::class.java
                )
            startActivity(intent)
            finish()
        }
    }
}