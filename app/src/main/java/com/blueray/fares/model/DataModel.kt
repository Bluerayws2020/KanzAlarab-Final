package com.blueray.fares.model

import com.google.gson.annotations.SerializedName

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
    data class NoInternet(val exception: String) : NetworkResults<Nothing>()
    // can be added
//    data class Loading<T>(val data: T? = null) : NetworkResults<T>()

}

data class UserLoginModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: LoginModel
)


data class LoginModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("phone") val phone: String,
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
    val token: String
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
val videoUrl : String

)


