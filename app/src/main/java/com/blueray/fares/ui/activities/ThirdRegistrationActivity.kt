package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.fares.R
import com.blueray.fares.adapters.ActivitiesTypesAdapter
import com.blueray.fares.databinding.ActivityThirdRegistrationBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.ui.viewModels.AppViewModel

class ThirdRegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityThirdRegistrationBinding
    private val viewmodel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.signInBtn.setOnClickListener {

            startActivity(Intent(this,LoginActivity::class.java))
        }



        val editTexts = listOf(binding.bandName, binding.userNameEt,binding.passwordTxt,binding.ConfirmPassword,binding.phoneNumber,binding.email)

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





        if(RegistrationActivity.signupType == "1"){
            binding.banlabel.hide()
            binding.bandName.hide()

        }else{
            binding.banlabel.hide()
            binding.bandName.hide()
        }

        getCreateBandAccounts()
        getCreateAccounts()

        binding.nextBtn.setOnClickListener {
            if (binding.phoneNumber.text?.isEmpty() == false) {
                if (binding.userNameEt.text?.isEmpty() == false) {


                    if (binding.ConfirmPassword.text.toString() == binding.passwordTxt.text.toString()) {
showProgress()

                        if (RegistrationActivity.signupType == "1") {


                            viewmodel.retriveCreateAccount(
                                RegistrationActivity.firstName.toString(),
                                RegistrationActivity.lastName.toString(),
                                RegistrationActivity.genderId,
                                RegistrationActivity.natonalId,
                                RegistrationActivity.residantPlace,
                                ActivitiesTypesAdapter.selected_items.joinToString(","),
                                binding.userNameEt.text.toString(),
                                binding.email.text.toString(),
                                binding.phoneNumber.text.toString(),
                                binding.passwordTxt.text.toString(),
                                RegistrationActivity.barithDate
                            )
                        }else {
                            viewmodel.retriveBandName(

                   RegistrationActivity.bandName,
                    RegistrationActivity.natonalId,
                    RegistrationActivity.residantPlace,
                    ActivitiesTypesAdapter.selected_items.joinToString(","),
                    binding.userNameEt.text.toString(),
                    binding.email.text.toString(),
                    binding.phoneNumber.text.toString(),
                    binding.passwordTxt.text.toString(),
                    RegistrationActivity.squadNumber

                            )
                        }
                    } else {
                        binding.ConfirmPassword.setError("كلمة السر غير مطابقة")
                    }


                } else {
                    binding.userNameEt.setError("حقل ضروري ")
                }
            } else {
                binding.userNameEt.setError("حقل ضروري ")

            }
        }



    }


    private fun getCreateAccounts() {
        hideProgress()

        viewmodel.getCreateAccount().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {
         if (result.data.status.status == 200){
             saveUserData(result.data)

         }else {
             Toast.makeText(this,result.data.status.msg.toString(),Toast.LENGTH_LONG).show()
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

    private fun getCreateBandAccounts() {
        hideProgress()

        viewmodel.getBandAccount().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 200){
                        saveUserData(result.data)

                    }else {
                        Toast.makeText(this,result.data.status.msg.toString(),Toast.LENGTH_LONG).show()
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

    fun saveUserData(model:UserLoginModel){
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

        sharedPreferences.edit().apply {
            putString(HelperUtils.UID_KEY, model.data.uid)
            putString("role", model.data.uid)

            putString(HelperUtils.USERNAME, binding.userNameEt.text?.trim().toString())
            putString(HelperUtils.PASSWORD, binding.passwordTxt.text?.trim().toString())



        }.apply()
        startActivity(Intent(this,HomeActivity::class.java))

    }


    private fun hideProgress() {
        binding.progressBar.hide()

    }

    private fun showProgress() {
        binding.progressBar.show()
    }

}