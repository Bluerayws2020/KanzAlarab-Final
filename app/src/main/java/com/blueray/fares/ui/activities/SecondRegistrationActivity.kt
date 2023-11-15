package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.blueray.fares.R
import com.blueray.fares.adapters.ActivitiesTypesAdapter
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

        viewmodel.retriveCategory()

getCategory()
    }


    private fun getCategory() {
        hideProgress()

        viewmodel.getCategory().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {

                    adapter = ActivitiesTypesAdapter(result.data){
                        // handle click listener
                        Log.d("ERTYUIOP",it)

                        activtyIds = it
                    }

                    val chipsLayoutManager = ChipsLayoutManager.newBuilder(this).build()
                    binding.activitiesRv.adapter = adapter
                    binding.activitiesRv.layoutManager =chipsLayoutManager

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