package com.blueray.fares.api

import com.blueray.fares.model.NewAppendItItems

interface VideoClick {
    fun OnVideoClic(pos:List<NewAppendItItems>, position: Int)
}