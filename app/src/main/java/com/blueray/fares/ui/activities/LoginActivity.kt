package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityLoginBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.ui.viewModels.AppViewModel

class LoginActivity : BaseActivity() {
    private val viewmodel by viewModels<AppViewModel>()

    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }

        binding.userName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.password.requestFocus()
                true
            } else {
                false
            }
        }

        binding.password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {

HelperUtils.hideKeyBoard(this)
                true
            } else {
                false
            }
        }

        getLogin()
        binding.signInBtn.setOnClickListener {

            if (binding.userName.text?.isEmpty() == true || binding.password.text?.isEmpty() == true){
                Toast.makeText(this,"جميع الحقول مطلوبة",Toast.LENGTH_LONG).show()

            }else {
                showProgress()
                viewmodel.retriveLogin(binding.userName.text.toString(),binding.password.text.toString())

            }
        }

        
        binding.forgotPassword.setOnClickListener { 
            startActivity(Intent(this,ForgetPasswordFirstActivity::class.java))
        }

    }

    private fun getLogin() {
        hideProgress()

        viewmodel.getLogin().observe(this) { result ->
            hideProgress()

            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 200){
                        saveUserData(result.data)

                    }else {
                        Toast.makeText(this,result.data.status.msg.toString(), Toast.LENGTH_LONG).show()
                    }


                }

                is NetworkResults.Error -> {
                    Toast.makeText(this, result.exception.printStackTrace().toString(), Toast.LENGTH_LONG).show()

                    result.exception.printStackTrace()
                    hideProgress()
                }

                else -> hideProgress()
            }
        }
    }
    fun saveUserData(model: UserLoginModel){
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

        sharedPreferences.edit().apply {
            putString(HelperUtils.UID_KEY, model.datas.uid)
            putString("role", model.datas.uid)

            putString(HelperUtils.USERNAME, binding.userName.text?.trim().toString())
            putString(HelperUtils.PASSWORD, binding.password.text?.trim().toString())



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