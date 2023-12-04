package com.blueray.fares.api

import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.Item
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.UserUploadeDone
import com.blueray.fares.model.VideoDataModel
import com.blueray.fares.model.VideoResponse
import com.blueray.fares.model.ViewUserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiServices {
    @Multipart
    @POST("app/poet-registration")
    suspend fun addUser(

        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("nationality") nationality: RequestBody,
        @Part("country_of_residence") country_of_residence: RequestBody,
        @Part("types_of_activities") types_of_activities: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("barth_of_date") barth_of_date: RequestBody,




        ): UserLoginModel






    @Multipart
    @POST("app/band-registration")
    suspend fun addBandUser(

        @Part("band_name") band_name: RequestBody,
        @Part("nationality") nationality: RequestBody,
        @Part("country_of_residence") country_of_residence: RequestBody,
        @Part("types_of_activities") types_of_activities: RequestBody,
        @Part("user_name") user_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("number_of_band_members") barth_of_date: RequestBody,




        ): UserLoginModel


    @Multipart
    @POST("app/login")
    suspend fun userOtpLogin(
        @Part("user") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("lang") lang: RequestBody,

        ): UserLoginModel

    @Multipart
    @POST("app/add-poetry2")
    suspend fun userUplaodeVideo(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("viemo_link") viemo_link: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("type_of_activity") type_of_activity: RequestBody,

        ): UserUploadeDone
    @GET("app2/poetries")
    suspend fun getPoetries(        @Query("page")  page:String
    ): VideoDataModel

    @GET("app2/poetries")
    suspend fun getPoetriesForuser(
        @Query("uid")  uid:String,
        @Query("page_limit")  page_limit:String,

        @Query("state")  state:String,

    ): VideoDataModel



        @GET("en/app/user-info")
    suspend fun getUserInfo(
        @Query("uid")  uid:String,

    ):List<ViewUserLoginModel>


    @GET("app/nationality-list")
    suspend fun getNational( ): List<DropDownModel>





    @GET("app/country-list")
    suspend fun getCitis(         @Query("pid")  pid:String
    ): List<DropDownModel>
    @GET("app/country-list")
    suspend fun getCountry  (): List<DropDownModel>
    @GET("app/gender-list")
    suspend fun getGender( ): List<DropDownModel>


    @GET("app/activity-list")
    suspend fun getCategory(@Header("Authorization") authHeader: String ): List<DropDownModel>






    @GET
    suspend fun getVimeoVideo(
        @Url videoUrl: String,
        @Header("Authorization") authorizationToken:String,
    ): VimeoVideoModelV2









}