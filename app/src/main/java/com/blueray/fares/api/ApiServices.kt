package com.blueray.fares.api

import com.blueray.fares.model.Item
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface ApiServices {

    @Multipart
    @POST("app/login")
    suspend fun userOtpLogin(
        @Part("user") phone: RequestBody,
        @Part("password") password: RequestBody,
        @Part("lang") lang: RequestBody,

        ): UserLoginModel


    @GET("app/poetries")
    suspend fun getPoetries(@Header("Authorization") authHeader: String): List<Item>
    @GET
    suspend fun getVimeoVideo(
        @Url videoUrl: String,
        @Header("Authorization") authorizationToken:String,
    ): VimeoVideoModelV2
}