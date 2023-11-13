package com.blueray.fares.ui.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blueray.fares.api.NetworkRepository
import com.blueray.fares.helpers.HelperUtils
import com.blueray.fares.helpers.HelperUtils.showMessage
import com.blueray.fares.model.Item
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject





class AppViewModel(application: Application): AndroidViewModel(application) {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    private val language = HelperUtils.getLang(application.applicationContext)

    private val repo = NetworkRepository



    private val loginUserMessageLiveData = MutableLiveData<NetworkResults<UserLoginModel>>()

    private val getVideosLive = MutableLiveData<NetworkResults<List<Item>>>()

    private val vimeoVideoLiveData = MutableLiveData<NetworkResults<VimeoVideoModelV2>>()


    fun retriveMainVideos(){

        viewModelScope.launch{
            getVideosLive.value = repo.getVideos()
        }
    }

    fun getMainVideos() = getVideosLive


    fun retrieveVideoOption(videoUrl: String, vimeoToken:String) {
        viewModelScope.launch {
            val authToken="Bearer $vimeoToken"
            vimeoVideoLiveData.value = repo.getVimeoVideo(videoUrl, authToken)
        }
    }

    fun getVideoOption() = vimeoVideoLiveData

}