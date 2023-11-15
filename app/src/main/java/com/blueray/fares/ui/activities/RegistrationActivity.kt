package com.blueray.fares.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityRegisterationBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel
import java.util.Calendar

class RegistrationActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()
    private var genderList: ArrayList<DropDownModel> = ArrayList()
    private var natonalList: ArrayList<DropDownModel> = ArrayList()
    private lateinit var binding : ActivityRegisterationBinding

    companion object {
    var genderId = ""
        var natonalId = ""
        var firstName = ""
        var lastName = ""
        var barithDate = ""
        var residantPlace = ""

    }
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.dateOfBirthDatePicker.setOnClickListener {
    showDatePickerDialog()
}
        binding.nextBtn.setOnClickListener {
            firstName  =  binding.firstNameEt.text.toString()
            lastName  =  binding.lastNameEt.text.toString()
            barithDate  =  binding.dateOfBirthDatePicker.text.toString()
            firstName  =  binding.firstNameEt.text.toString()



            startActivity(Intent(this, SecondRegistrationActivity::class.java))


        }

        binding.individualLayout.show()

binding.radioOption1.isChecked = true
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Hide all layouts initially
            binding.individualLayout.show()
            when (checkedId) {
                R.id.radioOption1 ->
                {
                    binding.individualLayout.show()
                    binding.squadLayout.hide()
                }
                R.id.radioOption2 -> {
                    binding.individualLayout.hide()
                    binding.squadLayout.show()
                }
            }


        }

        showProgress()
        viewmodel.retriveGender()
        viewmodel.retriveNatonal()

        getNatonal()
        getGeneder()







    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            // Display Selected date in EditText
            binding.dateOfBirthDatePicker.setText("$dayOfMonth/${monthOfYear + 1}/$year")
        }, year, month, day)

        dpd.show()
    }
    private fun getNatonal() {
        viewmodel.getNatonal().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                        natonalList = result.data as ArrayList<DropDownModel>
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, natonalList.map { it.name })
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.nationalitySpinner.adapter = adapter
                    binding.nationalitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,   // <-- Allow view to be nullable
                            position: Int,
                            id: Long
                        ) {

                            natonalId = natonalList[position].id

                        }


                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }

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
    private fun getGeneder() {
        hideProgress()

        viewmodel.getGender().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                        genderList = result.data as ArrayList<DropDownModel>
                        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList.map { it.name })
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.genderSpinner.adapter = adapter

                    binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,   // <-- Allow view to be nullable
                            position: Int,
                            id: Long
                        ) {

                            genderId = natonalList[position].id

                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }
                    binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,   // <-- Allow view to be nullable
                            position: Int,
                            id: Long
                        ) {
                            if (natonalList.isNullOrEmpty()){

                            }else {
                                genderId = genderList[position].id
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
hideProgress()
                }

                else -> hideProgress()
            }
        }
    }





}