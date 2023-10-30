package com.blueray.fares.helpers

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

class CustomLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

    override fun calculateTimeForScrolling(dx: Int): Int {
        // Adjust this value to set your desired scroll duration in milliseconds
        return 200 // Change this to your desired duration
    }
}
