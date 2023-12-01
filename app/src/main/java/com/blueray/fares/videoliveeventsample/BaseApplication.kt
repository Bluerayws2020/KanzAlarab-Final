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

class BaseApplication : Application() {
    lateinit var prefManager: PrefManager
    private val _initResultLiveData = MutableLiveData<Event<Boolean>>()

    val initResultLiveData: LiveData<Event<Boolean>>
        get() = _initResultLiveData

    override fun onCreate() {
        super.onCreate()
        prefManager = PrefManager(applicationContext)
        val appId = "1CFCE912-F6CC-4F60-9FA1-FC4B3B61BA6A"
        if (appId == null) {
            _initResultLiveData.changeValue(Event(false))
            return
        }
        initSendbirdSDK("1CFCE912-F6CC-4F60-9FA1-FC4B3B61BA6A")
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