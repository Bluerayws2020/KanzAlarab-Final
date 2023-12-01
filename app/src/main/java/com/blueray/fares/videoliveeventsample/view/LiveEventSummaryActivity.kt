package com.blueray.fares.videoliveeventsample.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.blueray.fares.R
import com.blueray.fares.databinding.ActivityLiveEventSummaryBinding

import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_COVER_URL
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_DURATION
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_PEAK_PARTICIPANTS
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_TOTAL_PARTICIPANTS
import com.blueray.fares.videoliveeventsample.util.threeQuotes
import com.blueray.fares.videoliveeventsample.util.toTimerFormat


class LiveEventSummaryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveEventSummaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val coverUrl = intent.getStringExtra(INTENT_KEY_LIVE_EVENT_COVER_URL) ?: ""
        val totalParticipants = intent.getIntExtra(INTENT_KEY_LIVE_EVENT_TOTAL_PARTICIPANTS, 0)
        val peakParticipants = intent.getIntExtra(INTENT_KEY_LIVE_EVENT_PEAK_PARTICIPANTS, 0)
        val duration = intent.getLongExtra(INTENT_KEY_LIVE_EVENT_DURATION, 0L)
        binding = ActivityLiveEventSummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(coverUrl, totalParticipants, peakParticipants, duration)
    }

    private fun initView(coverUrl: String, totalParticipants: Int, peakParticipants: Int, duration: Long) {
        binding.ivClose.setOnClickListener { finish() }
        binding.ivLiveThumbnail.load(coverUrl) {
            error(R.drawable.profile_icon)
        }
        binding.tvLiveEventSummaryTotalParticipants.text = totalParticipants.threeQuotes()
        binding.tvLiveEventSummaryPeakParticipant.text = peakParticipants.threeQuotes()
        binding.tvLiveEventSummaryDuration.text = duration.toTimerFormat()
    }
}