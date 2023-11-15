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
import com.blueray.fares.model.DropDownModel
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

    private val natoinalLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val genderLive = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val categroLive = MutableLiveData<NetworkResults<List<DropDownModel>>>()


    private val createAccountLive = MutableLiveData<NetworkResults<UserLoginModel>>()

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



    fun retriveNatonal() {
        viewModelScope.launch {
            natoinalLiveData.value = repo.getNational()
        }
    }
    fun getNatonal() = natoinalLiveData





    fun retriveContry() {
        viewModelScope.launch {
            natoinalLiveData.value = repo.getCountry()
        }
    }
    fun getCountry() = natoinalLiveData




    fun retriveGender() {
        viewModelScope.launch {
            genderLive.value = repo.getGender()
        }
    }
    fun getGender() = genderLive


    fun retriveCategory() {
        viewModelScope.launch {
            categroLive.value = repo.getCategory()
        }
    }
    fun getCategory() = categroLive


 fun retriveCreateAccount(first_name: String,
                                 last_name: String,
                                 gender: String,
                                 nationality: String,
                                 country_of_residence: String,
                                 types_of_activities: String,
                                 user_name: String,
                                 email: String,
                                 phone: String,
                                 password:String,
                                 barth_of_date: String){
     viewModelScope.launch {

         createAccountLive.value = repo.addUser(
             first_name,
             last_name,
             gender,
             nationality,
             country_of_residence,
             types_of_activities,
             user_name,
             email,
             phone,
             password,
             barth_of_date
         )
     }
     }

    fun getCreateAccount() = createAccountLive






}