package com.blueray.fares.model

import android.graphics.Picture
import com.google.gson.annotations.SerializedName
import java.io.Serializable

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
    data class NoInternet(val exception: String) : NetworkResults<Nothing>()
    // can be added
//    data class Loading<T>(val data: T? = null) : NetworkResults<T>()

}

data class UserLoginModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("poet_data") val data: LoginModel,
    @SerializedName("data") val datas: LoginModel

)



data class ViewUserLoginModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("role") val role: String,
    @SerializedName("mail") val mail: String,
    @SerializedName("username") val username: String,
    @SerializedName("user_picture") val user_picture: String,
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("profile_data") val profile_data: Pprofile_data,

)
data class UserUploadeDone(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val datas: NidVideoUplaode

)
data class NidVideoUplaode(
    val nid:String
)


data class LoginModel(
    @SerializedName("id") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("user_picture") val user_picture: String,

    )



data class MessageModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String

)
data class Item(
    val id: String,
    val title: String,
    val file: String,
    val created: String,
    val uuid: String,
    val token: String,
    val vimeo_detials:VimeoVideoModelV2
)


data class DropDownModel(
    val id: String,
    val name: String,
)
data class VimeoVideoModelV2 (

    @SerializedName("uri") val uri : String,
    @SerializedName("name") val name : String,
    @SerializedName("description") val description : String,
    @SerializedName("type") val type : String,
    @SerializedName("link") val link : String,
    @SerializedName("player_embed_url") val player_embed_url : String,
    @SerializedName("duration") val duration : Int,
    @SerializedName("width") val width : Int,
    @SerializedName("language") val language : String,
    @SerializedName("height") val height : Int,
    @SerializedName("created_time") val created_time : String,
    @SerializedName("modified_time") val modified_time : String,
    @SerializedName("release_time") val release_time : String,
    @SerializedName("files") val files : List<VimeoFileModel>,
    @SerializedName("status") val status : String,
    @SerializedName("is_playable") val is_playable : Boolean,
    @SerializedName("has_audio") val has_audio : Boolean
)
data class VimeoFileModel (

    @SerializedName("rendition") val rendition : String,
    @SerializedName("height") val height : Int,
    @SerializedName("link") val link : String? = null,

    ) {
    override fun toString(): String {
        return rendition
    }
}

    data class NewAppendItItems (

val videoTitle : String,
val videoDesc : String,

val date : String,

val videoUrl : String,
val userId : String,
val userName : String,
val duration : Int,
val imageThum : String = "",
val firstName : String = "",
val lastName : String = "",
val bandNam : String = "",
val type : String = "",
val userPic : String = "",
val status : String = "",


):Serializable


data class VideoResponse(
    val id: String,
    val title: String,
    val created: String,
    val file: String,
    val uuid: String,
    val token: String,
    val moderation_state :String,
    val vimeo_detials: VimeoDetails,
    val auther: Author,

)
data class Pictures(
    val base_link:String
)
data class VimeoDetails(
    val uri: String,
    val name: String,
    val description: String,
    val type: String,
    val link: String,
    val player_embed_url: String,
    val duration: Int,
    val width: Int,
    val language: String,
    val height: Int,
    val files: List<VideoFile>,

    val pictures:Pictures
    // Add other fields as necessary...
)
data class VideoFile(

    val quality: String,
    val rendition: String,
    val type: String,
    val width: Int,
    val height: Int,
    val link: String,
    // Add other fields as necessary...
)

data class Author(
    val uid: String,
    val username: String,
    val type: String,


    val profile_data  :Pprofile_data
)

data class Pprofile_data(
    val first_name: String,
    val last_name: String,
    val user_picture: String,
    val band_name: String,
    val mail: String,

    )