package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.blueray.fares.model.RigsterModel
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.ui.viewModels.AppViewModel
import com.blueray.fares.videoliveeventsample.BaseApplication
import com.blueray.fares.videoliveeventsample.util.showToast
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.util.EventObserver

class ThirdRegistrationActivity : BaseActivity() {

    private lateinit var binding: ActivityThirdRegistrationBinding
    private val viewmodel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.signInBtn.setOnClickListener {

            startActivity(Intent(this, LoginActivity::class.java))
        }


//        val editTexts = listOf(binding.bandName, binding.userNameEt,binding.passwordTxt,binding.ConfirmPassword,binding.phoneNumber,binding.email)

//        editTexts.forEachIndexed { index, editText ->
//            editText.setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_NEXT -> {
//                        // Move to the next EditText if it's not the last one
//                        if (index < editTexts.size - 1) {
//                            editTexts[index + 1].requestFocus()
//                        }
//                        true
//                    }
//                    EditorInfo.IME_ACTION_DONE -> {
//                        // Hide the keyboard if it's the last EditText
//                        if (index == editTexts.size - 1) {
//                            HelperUtils.hideKeyBoard(this)
//                        }
//                        true
//                    }
//                    else -> false
//                }
//            }
//        }


        if (RegistrationActivity.signupType == "1") {
//            binding.banlabel.hide()
//            binding.bandName.hide()

        } else {
//            binding.banlabel.hide()
//            binding.bandName.hide()
        }

        getCreateAccounts()

        binding.nextBtn.setOnClickListener {
            if (binding.phoneNumber.text?.isEmpty() == false) {
//                if (binding.userNameEt.text?.isEmpty() == false) {
//
//
//                    if (binding.ConfirmPassword.text.toString() == binding.passwordTxt.text.toString()) {
//showProgress()

//                        if (RegistrationActivity.signupType == "1") {
showProgress()

                viewmodel.retriveCreateAccount(
                    RegistrationActivity.firstName.toString(),
                    RegistrationActivity.lastName.toString(),
                    RegistrationActivity.genderId,
                    RegistrationActivity.natonalId,
                    RegistrationActivity.residantPlace,
                    ActivitiesTypesAdapter.selected_items.joinToString(","),
                    RegistrationActivity.userName.toString(),
                    binding.email.text.toString(),
                    binding.phoneNumber.text.toString(),
                    RegistrationActivity.passwordTxt.toString(),
                    RegistrationActivity.barithDate
                )
//                        }else {
//                            viewmodel.retriveBandName(
//
//                   RegistrationActivity.bandName,
//                    RegistrationActivity.natonalId,
//                    RegistrationActivity.residantPlace,
//                    ActivitiesTypesAdapter.selected_items.joinToString(","),
//                    binding.userNameEt.text.toString(),
//                    binding.email.text.toString(),
//                    binding.phoneNumber.text.toString(),
//                    binding.passwordTxt.text.toString(),
//                    RegistrationActivity.squadNumber

//                      }      )
            }
//                    } else {
//                        binding.ConfirmPassword.setError("كلمة السر غير مطابقة")
//                    }


//                } else {
//                    binding.userNameEt.setError("حقل ضروري ")
        }
//            } else {
//                binding.userNameEt.setError("حقل ضروري ")

//            }
//        }


    }


    private fun getCreateAccounts() {
        hideProgress()

        viewmodel.getCreateAccount().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 200) {

                        Log.d("ERTY3UI5",result.data.datas.toString())
                        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
                        Log.d("TESTTTTLOOG",result.data.datas.uid)

                        sharedPreferences.edit().apply {
                            putString(HelperUtils.UID_KEY, result.data.datas.uid)
                            putString("role",result.data.datas.uid)

                            putString(HelperUtils.USERNAME, RegistrationActivity.userName)
                            putString(HelperUtils.PASSWORD, RegistrationActivity.passwordTxt)



                        }.apply()


                        Log.d("wertyuiop",HelperUtils.getUid(this))
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()

                    } else {
                        Toast.makeText(this, result.data.status.msg.toString(), Toast.LENGTH_LONG)
                            .show()
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

    private fun hideProgress() {
        binding.progressBar.hide()

    }

    private fun showProgress() {
        binding.progressBar.show()
    }
    fun saveUserData(model: RigsterModel){
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
Log.d("TESTTTTLOOG",model.toString())

        sharedPreferences.edit().apply {
            putString(HelperUtils.UID_KEY, model.uid)
            putString("role", model.type)

            putString(HelperUtils.USERNAME, RegistrationActivity.userName)
            putString(HelperUtils.PASSWORD, RegistrationActivity.passwordTxt)



        }.apply()
        startActivity(Intent(this,HomeActivity::class.java))
                    finish()

//        //            prefManager = PrefManager(this)
//        (application as BaseApplication).initResultLiveData.observe(this, EventObserver {
//            if (it) {
//                autoAuthenticate { isSucceed, e ->
//                    if (e != null) showToast(e)
//                    binding.progressBar.hide()
//                    startActivity(Intent(this,HomeActivity::class.java))
//                    finish()
//                }
//            } else {
//                showToast("يوجد خلل 4003")
//            }
//        })

    }

    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        binding.progressBar.show()
        val appId ="47918183-5186-4085-A042-489C9F4726BC"
        val userId = "65"
        val accessToken = "205349e679a5598c976d50d9a17093cc1651753b"

        if (appId == null || userId == null) {
            callback.invoke(false, null)
            return
        }

        val params = AuthenticateParams(userId, accessToken)
        SendbirdLive.authenticate(params) { user, e ->
            if (e != null || user == null) {
                callback.invoke(false, "${e?.message}")

                return@authenticate
            }
            callback.invoke(true, null)
        }
    }


}

//    private fun getCreateBandAccounts() {
//        hideProgress()

//        viewmodel.getBandAccount().observe(this) { result ->
//            hideProgress()
//
//            when (result) {
//                is NetworkResults.Success -> {
//                    if (result.data.status.status == 200){
//                        saveUserData(result.data)
//
//                    }else {
//                        Toast.makeText(this,result.data.status.msg.toString(),Toast.LENGTH_LONG).show()
//                    }
//
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

//    fun saveUserData(model:UserLoginModel){
//        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
//
//        sharedPreferences.edit().apply {
//            putString(HelperUtils.UID_KEY, model.data.uid)
//            putString("role", model.data.uid)
//
//            putString(HelperUtils.USERNAME, binding.userNameEt.text?.trim().toString())
//            putString(HelperUtils.PASSWORD, binding.passwordTxt.text?.trim().toString())
//
//
//
//        }.apply()
//        startActivity(Intent(this,HomeActivity::class.java))
//
//    }




//}