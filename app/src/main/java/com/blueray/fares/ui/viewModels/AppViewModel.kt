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
import com.blueray.fares.model.FollowingResponse
import com.blueray.fares.model.Item
import com.blueray.fares.model.MessageModel
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NotfiMain
import com.blueray.fares.model.RgetrationModel
import com.blueray.fares.model.RigsterModel
import com.blueray.fares.model.SearchDataModel
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.UserUploadeDone
import com.blueray.fares.model.VideoDataModel
import com.blueray.fares.model.VideoResponse
import com.blueray.fares.model.ViewUserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject





class AppViewModel(application: Application): AndroidViewModel(application) {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    private val language = HelperUtils.getLang(application.applicationContext)
    private val userId = HelperUtils.getUid(application.applicationContext)

    private val repo = NetworkRepository


    private val getFollowingLive = MutableLiveData<NetworkResults<FollowingResponse>>()
    private val getFollowerLive = MutableLiveData<NetworkResults<FollowingResponse>>()

    private val loginUserMessageLiveData = MutableLiveData<NetworkResults<UserLoginModel>>()

    private val getVideosLive = MutableLiveData<NetworkResults<VideoDataModel>>()

    private val getFlagContetntLive = MutableLiveData<NetworkResults<VideoDataModel>>()
    private val getNotficationLive = MutableLiveData<NetworkResults<NotfiMain>>()

    private val getSearchLive = MutableLiveData<NetworkResults<SearchDataModel>>()

    private val getUserVideosLive = MutableLiveData<NetworkResults<VideoDataModel>>()
    private val viewUserPrfofile = MutableLiveData<NetworkResults<List<ViewUserLoginModel>>>()

    private val vimeoVideoLiveData = MutableLiveData<NetworkResults<VimeoVideoModelV2>>()

    private val natoinalLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val coityLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val countyLiveData = MutableLiveData<NetworkResults<List<DropDownModel>>>()

    private val genderLive = MutableLiveData<NetworkResults<List<DropDownModel>>>()
    private val categroLive = MutableLiveData<NetworkResults<List<DropDownModel>>>()


    private val createAccountLive = MutableLiveData<NetworkResults<UserLoginModel>>()
    private val createAccountBandLive = MutableLiveData<NetworkResults<UserLoginModel>>()


    private val userUplaodeLoive = MutableLiveData<NetworkResults<UserUploadeDone>>()
    private val setActions = MutableLiveData<NetworkResults<MessageModel>>()

    fun retriveMainVideos(page:Int,pageLimit:Int,ishome:String){

        viewModelScope.launch{
            getVideosLive.value = repo.getVideos(userId,page,pageLimit,ishome)
        }
    }

    fun getMainVideos() = getVideosLive

    fun retriveFlagContent(flag:String){

        viewModelScope.launch{
            getFlagContetntLive.value = repo.getFlagContent(userId,flag)
        }
    }

    fun getFlagContent() = getFlagContetntLive


    fun retriveNotfication(){

        viewModelScope.launch{
            getNotficationLive.value = repo.getNotfication(userId)
        }
    }

    fun getNotifcation() = getNotficationLive







    fun retrivesearchTxt(txt:String){

        viewModelScope.launch{
            getSearchLive.value = repo.getSearchContent(userId,txt)
        }
    }

    fun getSerchData() = getSearchLive




    fun retriveUserUplaode(   title: String,
                              description: String,

                              viemo_link: String,
                              type_of_activity: String,){

        viewModelScope.launch{
            userUplaodeLoive.value = repo.userUplaodeVideo(title, description, viemo_link, userId, type_of_activity)
        }
    }

    fun getUplaodeVide() = userUplaodeLoive
    fun retriveFollowing(
        targetUidBody:String ){

        viewModelScope.launch{
            getFollowingLive.value = repo.getFollowing(userId,targetUidBody)
        }
    }

    fun getFollowing() = getFollowingLive


    fun retriveFollower(
        targetUidBody:String ){

        viewModelScope.launch{
            getFollowerLive.value = repo.getFollower(userId,targetUidBody)
        }
    }

    fun getFollower() = getFollowerLive





    fun retriveSetAction(
                                entityId: String,
                                entity_type: String,
                                flag_id: String,){

        viewModelScope.launch{
            setActions.value = repo.setUserActionPost(userId,entityId,entity_type,flag_id)
        }
    }

    fun getSetAction() = setActions


    fun retriveUserVideos(
        state:String,pagesize:String,user_profile_uid:String,
        is_home:String,
        page:String
    ){

        viewModelScope.launch{
            getUserVideosLive.value = repo.getVideosForUser(userId,state,pagesize,user_profile_uid,is_home,page)
        }
    }

    fun getUserVideos() = getUserVideosLive

    fun retriveViewUserProfile (){

        viewModelScope.launch{
            viewUserPrfofile.value = repo.getUserProfile(userId)

        }
    }

    fun getUserProfile() = viewUserPrfofile

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
            countyLiveData.value = repo.getCountry()
        }
    }
    fun getCountry() = countyLiveData



    fun retriveCity(cid:String) {
        viewModelScope.launch {
            coityLiveData.value = repo.getCity(cid)
        }
    }
    fun getCity() = coityLiveData

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


fun retriveBandName(    band_name: String,
                        nationality: String,
                        country_of_residence: String,
                        types_of_activities: String,
                        user_name: String,
                        email: String,
                        phone: String,
                        password:String,
                        number_of_band_members: String,){
    viewModelScope.launch {
        createAccountBandLive.value = repo.addBand(
            band_name,
            nationality,
            country_of_residence,
            types_of_activities,
            user_name,
            email,
            phone,
            password,
            number_of_band_members
        )
    }

    }
fun getBandAccount() = createAccountBandLive


    fun retriveLogin(userName:String,password:String) {
        viewModelScope.launch {
            loginUserMessageLiveData.value = repo.userOtpLogin(userName,password,language)
        }
    }
    fun getLogin() = loginUserMessageLiveData



}