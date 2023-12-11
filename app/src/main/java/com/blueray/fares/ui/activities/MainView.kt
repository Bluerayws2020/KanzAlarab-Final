package com.blueray.fares.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.blueray.fares.ui.viewModels.AppViewModel
import com.blueray.fares.databinding.ActivityMainBinding
import com.blueray.fares.databinding.MainViewBinding
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.videoliveeventsample.BaseApplication
import com.blueray.fares.videoliveeventsample.util.PrefManager
import com.blueray.fares.videoliveeventsample.util.showToast
import com.sendbird.live.AuthenticateParams
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.util.EventObserver

class MainView : BaseActivity() {

    private val viewModel : AppViewModel by viewModels()

    private lateinit var binding : MainViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)





        binding.poetButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.guestBtn.setOnClickListener {

//            prefManager = PrefManager(this)
            (application as BaseApplication).initResultLiveData.observe(this, EventObserver {
                if (it) {
                    autoAuthenticate { isSucceed, e ->
                        if (e != null) showToast(e)
                        val intent = Intent(this, HomeActivity::class.java)
//                        else showToast("يوجد خلل 4003")

                        startActivity(intent)
                        finish()
                    }
                } else {
             showToast("يوجد خلل 4003")
                }
            })


        }


    }

    private fun autoAuthenticate(callback: (Boolean, String?) -> Unit) {
        val appId = "47918183-5186-4085-A042-489C9F4726BC"
        val userId = "Guest"
//        val accessToken = "a509c1fbce3f09483f6b3196bb6f9368757a72ac"
        val accessToken = ""

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