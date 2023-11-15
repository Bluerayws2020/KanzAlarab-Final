package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
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

        binding.nextBtn.setOnClickListener {
            if (binding.phoneNumber.text?.isEmpty() == false) {
                if (binding.userNameEt.text?.isEmpty() == false) {


                    if (binding.ConfirmPassword.text.toString() == binding.squadNameEt.text.toString()) {
showProgress()
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
                            binding.squadNameEt.text.toString(),
                            RegistrationActivity.barithDate
                        )

                    } else {
                        binding.ConfirmPassword.setError("password dose not matching")
                    }


                } else {
                    binding.userNameEt.setError("requred Feaild")
                }
            } else {
                binding.userNameEt.setError("requred Feaild")

            }
        }


        getCreateAccounts()

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

    fun saveUserData(model:UserLoginModel){
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

        sharedPreferences.edit().apply {
            putString(HelperUtils.UID_KEY, model.data.uid)
            putString("role", model.data.uid)

            putString(HelperUtils.USERNAME, binding.userNameEt.text?.trim().toString())
            putString(HelperUtils.PASSWORD, binding.squadNameEt.text?.trim().toString())



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