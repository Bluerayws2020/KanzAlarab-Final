package com.blueray.fares.api

interface VideoPlaybackControl {
    fun pauseAllVideos()
    fun playVideoAtPosition(position: Int)
}
