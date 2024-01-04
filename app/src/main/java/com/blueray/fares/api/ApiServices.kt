package com.blueray.fares.api

import com.blueray.fares.model.DropDownModel
import com.blueray.fares.model.FollowingResponse
import com.blueray.fares.model.MessageModel
import com.blueray.fares.model.MessageModelData
import com.blueray.fares.model.NotfiMain
import com.blueray.fares.model.SearchDataModel
import com.blueray.fares.model.UserLoginModel
import com.blueray.fares.model.UserUploadeDone
import com.blueray.fares.model.VideoDataModel
import com.blueray.fares.model.ViewUserLoginModel
import com.blueray.fares.model.VimeoVideoModelV2
import com.blueray.fares.model.checkUserFollowData
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
    @POST("ar/app/poet-registration")
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
    @POST("ar/app/add-poetry2")
    suspend fun userUplaodeVideo(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("viemo_link") viemo_link: RequestBody,
        @Part("uid") uid: RequestBody,
        @Part("type_of_activity") type_of_activity: RequestBody,

        ): UserUploadeDone





    @Multipart
    @POST("app/check-user-permission")
    suspend fun checkUserPermission(
        @Part("uid") uid: RequestBody,

        ): UserUploadeDone

    @Multipart
    @POST("app/following-users")
    suspend fun getFollowing(
        @Part("uid") uid: RequestBody,
        @Part("target_uid") target_uid:RequestBody


        ): FollowingResponse

    @Multipart
    @POST("app/followers")
    suspend fun getFollowers(
        @Part("uid") uid: RequestBody,
        @Part("target_uid") target_uid:RequestBody


    ): FollowingResponse


    @Multipart
    @POST("app/flag-action")
    suspend fun ActionPost(
        @Part("uid") uid: RequestBody,
        @Part("entity_id") entity_id: RequestBody,
        @Part("entity_type") entity_type: RequestBody,
        @Part("flag_id") flag_id: RequestBody,

        ): MessageModel



    @GET("app2/poetries")
    suspend fun getPoetries(
                    @Query("uid")  uid:String,
                    @Query("page")  page:String,
                    @Query("page_limit")  page_limit:String,
                    @Query("is_home")is_home:String,


        ): VideoDataModel






    @GET("app2/poetries")
    suspend fun getPoetriesForuser(
        @Query("uid")  uid:String,
        @Query("page_limit")  page_limit:String,

        @Query("state")  state:String,
        @Query("user_profile_uid")user_profile_uid:String,
        @Query("is_home")is_home:String,
        @Query("page")page:String



    ): VideoDataModel






    @Multipart
    @POST("app/flag-conents-list")
    suspend fun getFlagContent(


        @Part("uid") uid: RequestBody,
    @Part("flag_id") flag_id: RequestBody,

    ): VideoDataModel


    @Multipart
    @POST("ar/app/delete-poetry")
    suspend fun deletVideo(


        @Part("uid") uid: RequestBody,
        @Part("id") id: RequestBody,

        ): MessageModelData
@Multipart
    @POST("app/notifications")
    suspend fun getNotfi(
        @Part("uid") uid: RequestBody,

    ): NotfiMain


    @Multipart
    @POST("app/search")
    suspend fun getSearch(


        @Part("uid") uid: RequestBody,
        @Part("search_key") SearchDataModel: RequestBody,

        ): SearchDataModel





    @Multipart
    @POST("app/check-user-follow")
    suspend fun checkUserFollow(


        @Part("uid") uid: RequestBody,
        @Part("target_uid") target_uid: RequestBody,

        ): checkUserFollowData

    @GET("app/user-info")
    suspend fun getUserInfo(
        @Query("uid")  uid:String,

    ):List<ViewUserLoginModel>



    @Multipart
    @POST("app/edit-poet-profile")
    suspend fun editProfile(
        @Part("uid") uid: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part  img :   MultipartBody.Part,
        @Part("barth_of_date") barth_of_date: RequestBody,
    @Part("gender") gender: RequestBody,



        ):MessageModelData
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