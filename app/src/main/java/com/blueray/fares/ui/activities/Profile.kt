package com.blueray.fares.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.window.SplashScreen
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityMainBinding
import com.blueray.fares.databinding.ActivityProfileBinding
import com.blueray.fares.databinding.EditProfilessBinding
import com.blueray.fares.databinding.FragmentYourChannelBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.ui.viewModels.AppViewModel
import com.bumptech.glide.Glide

class Profile : BaseActivity() {

    private lateinit var binding : EditProfilessBinding
    private val mainViewModel by viewModels<AppViewModel>()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = EditProfilessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.retriveViewUserProfile()
getUserProifle()

        binding.tollbars .logout.show()
        binding.tollbars .logout.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(title)
                builder.setMessage("هل انت متاكد من تسجيل الخروج ؟")

                builder.setPositiveButton("نعم") { dialog, _ ->
                    val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)

                    sharedPreferences.edit().apply {
                        putString(HelperUtils.UID_KEY, "0")




                    }.apply()            // go to home activity
                    startActivity(Intent(this,com.blueray.fares.ui.activities.SplashScreen::class.java))
                }

                builder.setNegativeButton("لا") { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()



        }




    }


    fun getUserProifle(){


        mainViewModel.getUserProfile().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    val  data = result.data[0]
                    Glide.with(this).load(result.data[0].user_picture).placeholder(R.drawable.logo2).into(binding.userImagee)
                    binding.userNameEt.setText(data.username)

                    binding.phoneTxt.setText(data.phone_number)
                    binding.emailTxt.setText(data.profile_data.mail)
binding.nameEt.setText(data.profile_data.first_name + "\t" + data.profile_data.last_name)

binding.genderEt.setText(data.profile_data.gender.toString())
                    binding.birthDateTxt.setText(data.profile_data.birth_date.toString())

                }

                is NetworkResults.Error -> {

                    Log.d("ERRRRor",result.exception.toString())
                }
                is NetworkResults.NoInternet -> TODO()
            }
        }

    }
}