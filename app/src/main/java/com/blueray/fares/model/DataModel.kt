package com.blueray.fares.model

import com.google.gson.annotations.SerializedName

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
    data class NoInternet(val exception: String) : NetworkResults<Nothing>()
    // can be added
//    data class Loading<T>(val data: T? = null) : NetworkResults<T>()
}


