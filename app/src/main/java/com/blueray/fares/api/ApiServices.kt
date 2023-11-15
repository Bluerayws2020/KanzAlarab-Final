package com.blueray.fares.api

import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.Item
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
    @POST("app/login")
    suspend fun userOtpLogin(
        @Part("user") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("lang") lang: RequestBody,

        ): UserLoginModel


    @GET("app/poetries")
    suspend fun getPoetries(@Header("Authorization") authHeader: String): List<Item>
    @GET("app/nationality-list")
    suspend fun getNational( ): List<DropDownModel>


    @GET("app/country-list")
    suspend fun getCitis( ): List<DropDownModel>

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