package com.blueray.fares.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityRegisterationBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import java.util.Calendar

class RegistrationActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()
    private var genderList: ArrayList<DropDownModel> = ArrayList()
    private var countryList: ArrayList<DropDownModel> = ArrayList()
    private var cityList: ArrayList<DropDownModel> = ArrayList()

    private var natonalList: ArrayList<DropDownModel> = ArrayList()
    private lateinit var binding : ActivityRegisterationBinding
    val numbersList: ArrayList<Int> = (1..90).toList() as ArrayList<Int>

    companion object {
    var genderId = ""
        var natonalId = ""
        var firstName = ""
        var lastName = ""
        var barithDate = ""
        var residantPlace = ""

        var signupType = ""
        var passwordTxt  = ""
        var userName  = ""


        var countryID = ""
        var cityId = ""
        var numberOfBand  = 0
        var bandName  = ""

    }
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            binding = ActivityRegisterationBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.dateOfBirthDatePicker.setOnClickListener {
    showDatePickerDialog()
}
            var count = 1

binding.addItem.setOnClickListener{
    count++
    binding.itemCount.text = count.toString()
}
            binding.removeItem.setOnClickListener{
                if (count == 1){

                }else {
                    count--

                    binding.itemCount.text = count.toString()
                }
            }


            binding.signInBtn.setOnClickListener {

                startActivity(Intent(this,LoginActivity::class.java))
            }
        binding.nextBtn.setOnClickListener {
            firstName  =  binding.firstNameEt.text.toString()
            lastName  =  binding.lastNameEt.text.toString()
            barithDate  =  binding.dateOfBirthDatePicker.text.toString()
            firstName  =  binding.firstNameEt.text.toString()
            if (binding.female.isChecked){
                genderId = "Female"
            }else {
                genderId = "Male"

            }
            bandName =  binding.squadNameEt.text.toString()
//            one
if (signupType == "1"){




if (binding.firstNameEt.text?.isEmpty() == true){
    binding.firstNameEt.setError("حقل ضروري ")
}
    else if (binding.lastNameEt.text?.isEmpty() == true){
    binding.lastNameEt.setError("حقل ضروري ")

}
else if (binding.dateOfBirthDatePicker.text?.isEmpty() == true){
    binding.dateOfBirthDatePicker.setError("حقل ضروري ")

}else {
    passwordTxt = binding.password.text.toString()
    userName =  binding.userNameEt.text.toString()
    startActivity(Intent(this, SecondRegistrationActivity::class.java))

}


}
//band
else
{
    if (binding.squadNameEt.text?.isEmpty() == true){
        binding.squadNameEt.setError("حقل ضروري ")
    }
    else if (binding.itemCount.text == "1") {
Toast.makeText(this,"عدد اشخاص الفرقة اكثر من واحد",Toast.LENGTH_LONG).show()
    }else {

        startActivity(Intent(this, SecondRegistrationActivity::class.java))

    }


    }



        }



            val editTexts = listOf(binding.firstNameEt, binding.lastNameEt)

            editTexts.forEachIndexed { index, editText ->
                editText.setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_NEXT -> {
                            // Move to the next EditText if it's not the last one
                            if (index < editTexts.size - 1) {
                                editTexts[index + 1].requestFocus()
                            }
                            true
                        }
                        EditorInfo.IME_ACTION_DONE -> {
                            // Hide the keyboard if it's the last EditText
                            if (index == editTexts.size - 1) {
                            HelperUtils.hideKeyBoard(this)
                            }
                            true
                        }
                        else -> false
                    }
                }
            }


            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, numbersList)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.numberBand.adapter = adapter

            binding.numberBand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,   // <-- Allow view to be nullable
                    position: Int,
                    id: Long
                ) {

//                    numberOfBand = numbersList[position]

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }


            getCountryLIst()
            viewmodel.retriveContry()
            getCityList()


        binding.individualLayout.show()
            signupType =  "1"
binding.radioOption1.isChecked = true
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Hide all layouts initially
            binding.individualLayout.show()
            when (checkedId) {
                R.id.radioOption1 ->
                {
//                    1==>indi 2===>squad
                    signupType =  "1"
                    binding.individualLayout.show()
                    binding.squadLayout.hide()
                    numberOfBand = count.toInt()
                }
                R.id.radioOption2 -> {
                    signupType =  "2"

                    binding.individualLayout.hide()
                    binding.squadLayout.show()

                    numberOfBand = count.toInt()


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
//                        binding.nationalitySpinner.adapter = adapter
//                    binding.nationalitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(
//                            parent: AdapterView<*>,
//                            view: View?,   // <-- Allow view to be nullable
//                            position: Int,
//                            id: Long
//                        ) {
//
//                            natonalId = natonalList[position].id
//
//                        }
//
//
//                        override fun onNothingSelected(parent: AdapterView<*>) {
//                        }
//                    }

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
//                        binding.genderSpinner.adapter = adapter

//                    binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(
//                            parent: AdapterView<*>,
//                            view: View?,   // <-- Allow view to be nullable
//                            position: Int,
//                            id: Long
//                        ) {
//
//                            genderId = genderList[position].id
//
//                        }
//
//                        override fun onNothingSelected(parent: AdapterView<*>) {
//                        }
//                    }
//                    binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(
//                            parent: AdapterView<*>,
//                            view: View?,   // <-- Allow view to be nullable
//                            position: Int,
//                            id: Long
//                        ) {
//                            if (natonalList.isNullOrEmpty()){
//
//                            }else {
//                                genderId = genderList[position].id
//                            }
//                        }
//
//                        override fun onNothingSelected(parent: AdapterView<*>) {
//                        }
//                    }

                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }




    private fun getCountryLIst () {
        hideProgress()

        viewmodel.getCountry().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                    countryList = result.data as ArrayList<DropDownModel>
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryList.map { it.name })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.countryForBand.adapter = adapter

                    binding.countryForBand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,   // <-- Allow view to be nullable
                            position: Int,
                            id: Long
                        ) {

                            countryID = countryList[position].id
                            viewmodel.retriveCity(countryList[position].id)


                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                        }
                    }

//                    binding.countrySpinner.adapter = adapter

//                    binding.countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(
//                            parent: AdapterView<*>,
//                            view: View?,   // <-- Allow view to be nullable
//                            position: Int,
//                            id: Long
//                        ) {
//
//                            countryID = countryList[position].id
//                            viewmodel.retriveCity(countryList[position].id)
//
//
//                        }
//
//                        override fun onNothingSelected(parent: AdapterView<*>) {
//                        }
//                    }



                }

                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }





    private fun getCityList () {
        hideProgress()

        viewmodel.getCity().observe(this) { result ->

            when (result) {
                is NetworkResults.Success -> {
                    hideProgress()
                    cityList = result.data as ArrayList<DropDownModel>
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cityList.map { it.name })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    binding.citySpinner.adapter = adapter

//                    binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                        override fun onItemSelected(
//                            parent: AdapterView<*>,
//                            view: View?,   // <-- Allow view to be nullable
//                            position: Int,
//                            id: Long
//                        ) {
//
//                            cityId = cityList[position].id
//
//
//                        }
//
//                        override fun onNothingSelected(parent: AdapterView<*>) {
//                        }
//                    }

                    binding.cityForBand.adapter = adapter

                    binding.cityForBand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,   // <-- Allow view to be nullable
                            position: Int,
                            id: Long
                        ) {

                            cityId = cityList[position].id

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