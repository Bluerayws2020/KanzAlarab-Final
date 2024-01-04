package com.blueray.fares.videoliveeventsample

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sendbird.android.SendbirdChat
import com.sendbird.android.exception.SendbirdException
import com.sendbird.android.handler.InitResultHandler
import com.sendbird.android.params.InitParams
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.util.Event
import com.blueray.fares.videoliveeventsample.util.PrefManager
import com.blueray.fares.videoliveeventsample.util.changeValue
import com.onesignal.OneSignal

class BaseApplication : Application() {
    lateinit var prefManager: PrefManager
    private val _initResultLiveData = MutableLiveData<Event<Boolean>>()
     val ONESIGNAL_APP_ID = "366ee19d-e3cc-43ba-b944-b53ac64c6d3a"

    val initResultLiveData: LiveData<Event<Boolean>>
        get() = _initResultLiveData

    override fun onCreate() {
        super.onCreate()
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization

        OneSignal.initWithContext(this)
        OneSignal.setAppId(ONESIGNAL_APP_ID)
        prefManager = PrefManager(applicationContext)
        val appId = "6A2870E9-4E98-4044-85DE-24DF3DDECB4B"
        if (appId == null) {
            _initResultLiveData.changeValue(Event(false))
            return
        }
        initSendbirdSDK("6A2870E9-4E98-4044-85DE-24DF3DDECB4B")
    }

    private fun initSendbirdSDK(appId: String) {
        SendbirdChat.init(InitParams(appId, applicationContext, true), object : InitResultHandler {
            override fun onInitFailed(e: SendbirdException) {
                _initResultLiveData.changeValue(Event(false))
            }

            override fun onInitSucceed() {
                val params = com.sendbird.live.InitParams(appId, applicationContext)
                SendbirdLive.init(params, object : com.sendbird.live.handler.InitResultHandler {
                    override fun onInitFailed(e: com.sendbird.webrtc.SendbirdException) {
                        _initResultLiveData.changeValue(Event(false))
                    }

                    override fun onInitSucceed() {
                        _initResultLiveData.changeValue(Event(true))
                    }

                    override fun onMigrationStarted() {
                    }
                })
            }

            override fun onMigrationStarted() {
            }
        })
    }
}