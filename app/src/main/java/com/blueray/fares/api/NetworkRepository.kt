package com.blueray.fares.api

import android.util.Base64
import android.util.Log
import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.FollowingResponse
import com.blueray.fares.model.MessageModel
import com.blueray.fares.model.MessageModelData
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.NotfiMain
import com.blueray.fares.model.RgetrationModel
import com.blueray.fares.model.SearchDataModel
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.UserUploadeDone
import com.blueray.fares.model.VideoDataModel
import com.blueray.fares.model.ViewUserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import com.blueray.fares.model.checkUserFollowData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File
import java.lang.Exception


object NetworkRepository {
    private fun String.toFormBody() = toRequestBody("multipart/form-data".toMediaTypeOrNull())


    suspend fun userOtpLogin(
        user: String,
        password: String,

        language: String,


    ): NetworkResults<UserLoginModel> {
        return withContext(Dispatchers.IO){
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userBody = user.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.userOtpLogin(
                   userBody,passwordBody,
                    languageBody,
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun userUplaodeVideo(
        title: String,
        description: String,

        viemo_link: String,
        uid: String,
        type_of_activity: String,


        ): NetworkResults<UserUploadeDone> {
        return withContext(Dispatchers.IO){
            val titleBody = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val viemo_linkBody = viemo_link.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val type_of_activityBody = type_of_activity.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.userUplaodeVideo(
               titleBody,descriptionBody,viemo_linkBody,uidBody,type_of_activityBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getFollowing(
        uid: String,
        targetUid: String,



        ): NetworkResults<FollowingResponse> {
        return withContext(Dispatchers.IO){
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

val targetUidBody = targetUid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getFollowing(
                    uidBody,targetUidBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getFollower(
        uid: String,
        targetUid: String,



        ): NetworkResults<FollowingResponse> {
        return withContext(Dispatchers.IO){
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val targetUidBody = targetUid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getFollowers(
                    uidBody,targetUidBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun setUserActionPost(
        uid: String,
        entityId: String,
        entity_type: String,
        flag_id: String,


        ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO){
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val entityIdBody = entityId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val entity_typeBody = entity_type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val flag_idBody = flag_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.ActionPost(
uidBody,entityIdBody,entity_typeBody,flag_idBody


                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getVideos(
        uid: String,

        page:Int,
        pageLimit:Int,
        ishome:String

    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getPoetries(uid,
                    page.toString() ,pageLimit.toString(),ishome
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getFlagContent(
        uid: String,

        flagId:String,


    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val flagIDBody = flagId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getFlagContent(uidBody,
                    flagIDBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun getDeletVideos(
        uid: String,

        id:String,


        ): NetworkResults<MessageModelData> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val idBody = id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.deletVideo(uidBody,
                    idBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }




    suspend fun getNotfication(
        uid: String,



        ): NetworkResults<NotfiMain> {
        return withContext(Dispatchers.IO) {

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getNotfi(
                    uidBody,
                )
                Log.d("notifications",results.datass.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("notifications",e.localizedMessage.toString())

                NetworkResults.Error(e)
            }
        }

    }
        suspend fun getSearchContent(
        uid: String,

        search_key:String,


        ): NetworkResults<SearchDataModel> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val search_keyBody = search_key.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.getSearch(
                    uidBody,
                    search_keyBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }





    suspend fun getVideosForUser(
uid:String
,state:String,pagesize:String,user_profile_uid:String,
is_home:String,
page:String

    ): NetworkResults<VideoDataModel> {
        return withContext(Dispatchers.IO){

//                    ,pagesize.toString()


            try {
                val results = ApiClient.retrofitService.getPoetriesForuser(
                    uid,
                    pagesize,
                    state.toString(),
                    user_profile_uid,
                    is_home,
                    page
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getUserProfile(
        uid:String

    ): NetworkResults<List<ViewUserLoginModel>> {
        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getUserInfo(
             uid
                )


                Log.d("TEsst",results.toString())
                NetworkResults.Success(results)
            } catch (e: Exception){
                Log.d("TEsst100",e.localizedMessage.toString())

                NetworkResults.Error(e)

            }
        }
    }





    suspend fun getNational(



    ): NetworkResults<List<DropDownModel>> {


        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getNational(
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getCountry(



    ): NetworkResults<List<DropDownModel>> {
        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getCountry(
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }




    suspend fun getCity(

cid:String

    ): NetworkResults<List<DropDownModel>> {
        return withContext(Dispatchers.IO){

            val cidBody = cid.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.getCitis(
                    cid
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }



    suspend fun getEditProfile(

        uid:String,
        first_name:String,
        last_name:String,
        email:String,
        phone:String,
        img:File,
        gender:String,
        barth_of_date:String,




        ): NetworkResults<MessageModelData> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val first_nameBody = first_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val last_nameBody = last_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val genderBody = gender.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val barth_of_dateBody = barth_of_date.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val commercial_recordBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), img)
            val commercial_record_part  =  MultipartBody.Part.createFormData("img", img.name, commercial_recordBody)

            try {
                val results = ApiClient.retrofitService.editProfile(
                uidBody,first_nameBody,last_nameBody,emailBody,phoneBody,commercial_record_part,genderBody,barth_of_dateBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getCheckUserFollow(

        uid:String,
        targetFollow:String


    ): NetworkResults<checkUserFollowData> {
        return withContext(Dispatchers.IO){

            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val targetFollowUid = targetFollow.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.checkUserFollow(
                    uidBody,targetFollowUid
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getGender(



    ): NetworkResults<List<DropDownModel>> {
        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getGender(
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun getCategory(



    ): NetworkResults<List<DropDownModel>> {
        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getCategory(
                    createBasicAuthHeader("h12","12")

                )
                NetworkResults.Success(results)
            } catch (e: Exception){
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun addUser(
        first_name: String,
        last_name: String,
        gender: String,
        nationality: String,
        country_of_residence: String,
        types_of_activities: String,
        user_name: String,
        email: String,
        phone: String,
        password:String,
        barth_of_date: String,
    ): NetworkResults<UserLoginModel> {

        return withContext(Dispatchers.IO) {
            val first_nameBody = first_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val last_nameBody = last_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val genderBody = gender.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nationalityBody = nationality.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val country_of_residenceBody = country_of_residence.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val types_of_activitiesBody = types_of_activities.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val barth_of_dateBody = barth_of_date.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())





            try {
                val results = ApiClient.retrofitService.addUser(
                      first_nameBody,last_nameBody,genderBody,nationalityBody,country_of_residenceBody,types_of_activitiesBody,user_nameBody,emailBody,phoneBody,passwordBody,barth_of_dateBody

                    )


                Log.d("DONE",results.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDONE",e.toString())

                NetworkResults.Error(e)
            }
        }
    }







    suspend fun addBand(
        band_name: String,
        nationality: String,
        country_of_residence: String,
        types_of_activities: String,
        user_name: String,
        email: String,
        phone: String,
        password:String,
        number_of_band_members: String,
    ): NetworkResults<UserLoginModel> {

        return withContext(Dispatchers.IO) {

            val nationalityBody = nationality.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val country_of_residenceBody = country_of_residence.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val types_of_activitiesBody = types_of_activities.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val user_nameBody = user_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val number_of_band_membersBody = number_of_band_members.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val band_nameBody = band_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())





            try {
                val results = ApiClient.retrofitService.addBandUser(
                    band_nameBody,nationalityBody,country_of_residenceBody,types_of_activitiesBody,user_nameBody,emailBody,phoneBody,passwordBody,number_of_band_membersBody

                )


                Log.d("DONE",results.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDONE",e.toString())

                NetworkResults.Error(e)
            }
        }
    }

    fun createBasicAuthHeader(username: String, password: String): String {
        val auth = "$username:$password"
        val encodedAuth = Base64.encodeToString(auth.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
        return "Basic $encodedAuth"
    }


    suspend fun getVimeoVideo(videoUrl: String, vimeoToken:String): NetworkResults<VimeoVideoModelV2> {
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getVimeoVideo(videoUrl, vimeoToken)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }
}