package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.ActivitiesTypesAdapter
import com.blueray.fares.api.OnCategroryChose
import com.blueray.fares.databinding.ActivitySecoundRegistrationBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel

class SecondRegistrationActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()
companion object{
    var activtyIds = ""
}
    private lateinit var binding : ActivitySecoundRegistrationBinding
    private lateinit var adapter : ActivitiesTypesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecoundRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this,ThirdRegistrationActivity::class.java))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        viewmodel.retriveCategory()
getCategory()


            binding.signInBtn.setOnClickListener {
            if (activtyIds.isEmpty()){
            Toast.makeText(this,"يرجى اختيار تصنيف",Toast.LENGTH_LONG).show()

            }else {
            startActivity(Intent(this, LoginActivity::class.java))

            }


            }


        binding.notNow.setOnClickListener {
            startActivity(Intent(this, ThirdRegistrationActivity::class.java))

        }
    }


//    private fun getCategory() {
//        hideProgress()
//
//        viewmodel.getCategory().observe(this) { result ->
//
//            when (result) {
//                is NetworkResults.Success -> {
//
//
//                    adapter = ActivitiesTypesAdapter(result.data,object : OnCategroryChose {
//                        override fun onCategroyChose(id: String) {
//                            activtyIds = id
//                        }
//
//                    })
//
//                    val chipsLayoutManager = ChipsLayoutManager.newBuilder(this).build()
//                    binding.activitiesRv.adapter = adapter
//                    binding.activitiesRv.layoutManager =chipsLayoutManager
//
//                }
//
//                is NetworkResults.Error -> {
//                    result.exception.printStackTrace()
//                    hideProgress()
//                }
//
//                else -> hideProgress()
//            }
//        }
//    }
    private fun getCategory() {
        hideProgress()

        viewmodel.getCategory().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {

                    adapter = ActivitiesTypesAdapter(result.data, object : OnCategroryChose {
                        override fun onCategroyChose(id: String) {
                            activtyIds = id
                        }
                    })

                    // Replace ChipsLayoutManager with GridLayoutManager
                    val gridLayoutManager = GridLayoutManager(this, 2)
                    binding.activitiesRv.layoutManager = gridLayoutManager
                    binding.activitiesRv.adapter = adapter

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }


    private fun hideProgress() {
        binding.progressBar.hide()

    }

    private fun showProgress() {
        binding.progressBar.show()
    }
}