package com.blueray.fares.api

import android.util.Base64
import com.blueray.fares.model.Item
import com.blueray.fares.model.NetworkResults
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
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


    suspend fun getVideos(



        ): NetworkResults<List<Item>> {
        return withContext(Dispatchers.IO){



            try {
                val results = ApiClient.retrofitService.getPoetries(
                    createBasicAuthHeader("hamza","123")
                )
                NetworkResults.Success(results)
            } catch (e: Exception){
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