package com.blueray.fares.videoliveeventsample.view

import android.content.Intent
import android.view.View
import com.blueray.fares.R
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.sendbird.live.Host
import com.sendbird.live.LiveEvent
import com.sendbird.live.LiveEventState
import com.sendbird.live.ParticipantCountInfo
import com.blueray.fares.videoliveeventsample.util.displayFormat
import com.sendbird.uikit.consts.KeyboardDisplayType
import com.sendbird.uikit.fragments.OpenChannelFragment
import com.sendbird.webrtc.SendbirdException

const val KEY_LIVE_EVENT_LIKE_REACTION = "LIKE"

class LiveEventForParticipantActivity : LiveEventActivity() {

    override fun initLiveEventView() {
        super.initLiveEventView()
        binding.ivClose.setOnClickListener { finishLiveEvent(false) }
        if (liveEvent?.state == LiveEventState.READY) {
            showBanner(getString(R.string.banner_message_live_event_ready))
        }
    }

    override fun customOnBackPressed() {
        finishLiveEvent(false)
    }

    override fun finishLiveEvent(isEnded: Boolean) {
        super.finishLiveEvent(isEnded)
        liveEvent?.exit(null)
    }

    override fun getOpenChannelFragment(liveEventId: String) =

        OpenChannelFragment.Builder(liveEventId)
            .setCustomFragment(
                openChannelFragment.apply {
                    this.onHeaderRightButtonClickListener = View.OnClickListener {
                        val intent = Intent(this@LiveEventForParticipantActivity, ParticipantListActivity::class.java).apply {
                            putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEventId)
                        }
                        startActivity(intent)
                    }
                    this.headerRightButtonResourceId = R.drawable.icon_members
                    this.onHeaderReactionButtonClickListener = View.OnClickListener {
                        increaseReactionCount()
                    }
                    this.reactionButtonVisibility = View.VISIBLE
                }
            )
            .setKeyboardDisplayType(KeyboardDisplayType.Dialog)
            .useOverlayMode()
            .build()


    override var liveEventListenerImpl = object : LiveEventListenerImpl() {
        override fun onDisconnected(liveEvent: LiveEvent, e: SendbirdException) {
            finish()
        }
        override fun onHostConnected(liveEvent: LiveEvent, host: Host) {
            showBanner(null)
        }
        override fun onHostDisconnected(liveEvent: LiveEvent, host: Host) {
            showBanner(getString(R.string.banner_message_host_disconnected))
        }
        override fun onHostEntered(liveEvent: LiveEvent, host: Host) {
            updateToolbarView()
            addHostVideoView(host)
        }
        override fun onHostExited(liveEvent: LiveEvent, host: Host) {
            updateToolbarView()
            removeHostVideoView(host)
        }
        override fun onHostStartVideo(liveEvent: LiveEvent, host: Host) {
            updateHostVideoView(host)
        }
        override fun onHostStopVideo(liveEvent: LiveEvent, host: Host) {
            updateHostVideoView(host)
        }
        override fun onHostMuteAudio(liveEvent: LiveEvent, host: Host) {
            updateHostVideoView(host)
        }
        override fun onHostUnmuteAudio(liveEvent: LiveEvent, host: Host) {
            updateHostVideoView(host)
        }
        override fun onLiveEventEnded(liveEvent: LiveEvent) {
            finishLiveEvent(true)
        }
        override fun onLiveEventStarted(liveEvent: LiveEvent) {
            showBanner(null)
            startTimer(0L)
            setLiveStateView(liveEvent.state)
            binding.tvParticipantCount.text = liveEvent.participantCount.displayFormat()
        }

        override fun onParticipantCountChanged(liveEvent: LiveEvent, participantCountInfo: ParticipantCountInfo) {
            binding.tvParticipantCount.text = liveEvent.participantCount.displayFormat()
        }

        override fun onReactionCountUpdated(liveEvent: LiveEvent, key: String, count: Int) {
            runOnUiThread {
                distributeReactionAnimations(key, count)
            }
        }
    }

    private fun showBanner(text: String?) {
        if (text == null) {
            binding.tvBanner.visibility = View.GONE
        } else {
            binding.tvBanner.text = text
            binding.tvBanner.visibility = View.VISIBLE
        }
    }
    private fun increaseReactionCount() {
        liveEvent?.increaseReactionCount(KEY_LIVE_EVENT_LIKE_REACTION) increaseReactionCountLabel@{ reactionCountMap, e ->
            if (e != null) {
                return@increaseReactionCountLabel
            }
            reactionCountMap?.forEach {
                distributeReactionAnimations(it.key, it.value)
            }
        }
    }
}
