package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.ActivitiesTypesAdapter
import com.blueray.fares.databinding.ActivitySecoundRegistrationBinding

class SecondRegistrationActivity : BaseActivity() {

    private lateinit var binding : ActivitySecoundRegistrationBinding
    private lateinit var adapter : ActivitiesTypesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecoundRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this,ThirdRegistrationActivity::class.java))
        }

        prepareRv()


    }

    private fun prepareRv() {
        adapter = ActivitiesTypesAdapter(listOf()){
            // handle click listener
        }

        val chipsLayoutManager = ChipsLayoutManager.newBuilder(this).build()
        binding.activitiesRv.adapter = adapter
        binding.activitiesRv.layoutManager =chipsLayoutManager

    }
}