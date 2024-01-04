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

    @SerializedName("data") val datas: LoginModel

)

data class  RgetrationModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("poet_data") val data: RigsterModel,
)

data class ViewUserLoginModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("role") val role: String,
    @SerializedName("mail") val mail: String,
    @SerializedName("username") val username: String,
    @SerializedName("user_picture") val user_picture: String,
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("profile_data") val profile_data: Pprofile_data,
    @SerializedName("auther") val autherFoloower : AuthresFolow,

)
data class UserUploadeDone(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val datas: NidVideoUplaode

)
data class checkPermission(
    @SerializedName("data") val datas: NidVideoUplaode

)
data class NidVideoUplaode(
    val nid:String
)
data class RigsterModel(
    @SerializedName("uid") val uid: String,

    @SerializedName("type") val type: String,
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("user_picture") val user_picture: String,

    )



data class LoginModel(
    @SerializedName("id") val id: String,
    @SerializedName("uid") val uid: String,

    @SerializedName("type") val type: String,
    @SerializedName("phone_number") val phone_number: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("user_picture") val user_picture: String,

    )


data class FollowingResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String,
    @SerializedName("data") val data: List<FollowingList>,

    )
data class FollowingList(
    @SerializedName("uid") val uid: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("picture") val picture: String,
    @SerializedName("flag") var flag: Int,




    )
data class MessageModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String

)
data class MessageModelData(
    @SerializedName("msg") val status: MessageModel,

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


data class NotfiMain(
    @SerializedName("data") val datass: List<NofiItem>,

    )
data class NofiItem(
    @SerializedName("title") val title : String,
    @SerializedName("body") val body : String,
    @SerializedName("entity_id") val entity_id : String? = null,
    @SerializedName("flag_id") val flag_id : String? = null,
    @SerializedName("created") val created : String? = null,


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
var userFav : String = "",
var userSave : String = "",
        var target_user: TargetUsers? = null,
        var  video_counts: VideoCounts? = null,

val numOfFollowers: Int = 0,
val numOfFollowing: Int = 0,
val numOfLikes: Int =  0,
val nodeId:String = "",


):Serializable
//    {
//        // Toggle the like status
//        fun toggleLike() {
//            userFav = if (userFav == "1") "0" else "1"
//        }
//
//        // Toggle the save status
//        fun toggleSave() {
//            userSave = if (userSave == "1") "0" else "1"
//        }
//    }



data class VideoDataModel(
    @SerializedName("data") val datass: List<VideoResponse>,
   @SerializedName("target_user") val target_user : TargetUsers? = null,


)
data class SearchDataModel(
    @SerializedName("data") val datass: List<SarchItem>,


    )

data class checkUserFollowData(
    @SerializedName("data") val datass: CheckUserFollow,


    )

data class CheckUserFollow(
    @SerializedName("im_follow_him") val im_follow_him: String,
    @SerializedName("he_follow_me") val he_follow_me: String,
    @SerializedName("this_is_me") val this_is_me: String,

    )
data class  SarchItem(
    @SerializedName("uid") val uid: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("picture") val picture: String,

    @SerializedName("profile_data") val profile_data: Pprofile_data,
    
    @SerializedName("auther") val autherFoloower : AuthresFolow,


    )

data class  AuthresFolow(

    val numOfFollowers: Int,
    val numOfFollowing: Int,
    val numOfLikes: Int,
)
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
val video_actions_per_user:Video_actions_per_user,
    val video_counts:VideoCounts? = null

)

data class  VideoCounts(

    var like_count: Int,
    val  save_count: Int,


    )
data class TargetUsers(
    val im_in_user_profile: Int,
    val target_user_follow_flag: Int,
    val is_blocked: Int,
    val im_blocked: Int,

    )
data class Video_actions_per_user(
    val save: String,
    @SerializedName("like") val  favorites: Int,

    )
data class Pictures(
    val base_link:String = ""
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

    val pictures:Pictures? = null
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

    val numOfFollowers: Int,
    val numOfFollowing: Int,
    val numOfLikes: Int,


    val profile_data  :Pprofile_data
)

data class Pprofile_data(
    val first_name: String,
    val last_name: String,
    val user_picture: String,
    val band_name: String,
    val mail: String,
    val birth_date:String? = null ,
   @SerializedName("gender") val gender:String?

    )


data class Grid(
    val image :Int ,
    val sounds :Int ,

    val name:String,
    val price:String
)
