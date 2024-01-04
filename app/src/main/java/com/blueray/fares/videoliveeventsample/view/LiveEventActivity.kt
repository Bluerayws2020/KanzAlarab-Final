package com.blueray.fares.videoliveeventsample.view

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import coil.load
import com.blueray.fares.R
import com.blueray.fares.adapters.GiftAdapter
import com.blueray.fares.api.onGiftclicks
import com.blueray.fares.databinding.ActivityLiveEventBinding
import com.blueray.fares.databinding.BottomSheetLayoutBinding
import com.blueray.fares.databinding.CommentLayoutBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.model.Grid
import com.blueray.fares.ui.activities.GiftSheet
import com.blueray.fares.ui.activities.MyBottomSheetFragment
import com.sendbird.live.Host
import com.sendbird.live.LiveEvent
import com.sendbird.live.LiveEventListener
import com.sendbird.live.LiveEventRole
import com.sendbird.live.LiveEventState
import com.sendbird.live.ParticipantCountInfo
import com.sendbird.live.SendbirdLive
import com.blueray.fares.videoliveeventsample.adapter.HostAdapter
import com.blueray.fares.videoliveeventsample.util.CountUpTimer
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_COVER_URL
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_DURATION
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_PEAK_PARTICIPANTS
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_TOTAL_PARTICIPANTS
import com.blueray.fares.videoliveeventsample.util.displayFormat
import com.blueray.fares.videoliveeventsample.util.dp
import com.blueray.fares.videoliveeventsample.util.showToast
import com.blueray.fares.videoliveeventsample.util.toTimerFormat
import com.blueray.fares.videoliveeventsample.view.fragment.LiveEventOpenChannelFragment
import com.blueray.fares.videoliveeventsample.view.widget.ReactionConstants
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sendbird.uikit.fragments.OpenChannelFragment
import com.sendbird.webrtc.SendbirdException
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


abstract class LiveEventActivity : AppCompatActivity() {
    private var liveEventId: String? = null
    private var countUpTimer: CountUpTimer? = null
    private var cachedCoverUrl: String? = null
    private lateinit var adapter: HostAdapter
    protected lateinit var binding: ActivityLiveEventBinding
    protected var liveEvent: LiveEvent? = null
    private lateinit var dialog: BottomSheetDialog

    protected abstract var liveEventListenerImpl: LiveEventListenerImpl
    protected lateinit var openChannelFragment: LiveEventOpenChannelFragment

    private val singleThreadExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    private val reactionCountMap: ConcurrentHashMap<String, Int> = ConcurrentHashMap()



//    gifts
lateinit var adapterGifts: GiftAdapter
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveEventBinding.inflate(layoutInflater)
        liveEventId = intent.getStringExtra(INTENT_KEY_LIVE_EVENT_ID)
        setContentView(binding.root)
        openChannelFragment = LiveEventOpenChannelFragment()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                this@LiveEventActivity.customOnBackPressed()
            }
        })
        getLiveEvent()
//        initOpenChannelView()
        dialog = BottomSheetDialog(this)

        binding.gifts.setOnClickListener {
//            GiftSheet().show(supportFragmentManager, GiftSheet::class.java.simpleName)
            showBottomSheet()

        }








    }




    private fun showBottomSheet() {


        if (!dialog.isShowing) {

            var buyType = 0

            // initialize binding for bottom sheet
            val botBinding = BottomSheetLayoutBinding.inflate(layoutInflater)

            // Set rounded coaaa\rner drawable as background
            botBinding.root.background =
                ContextCompat.getDrawable(this, R.drawable.buttom_sheet_back)

            // address viewBinding to the bottomSheet dialog
            dialog.setContentView(botBinding.root)
            Glide.with(this@LiveEventActivity).load("https://firebasestorage.googleapis.com/v0/b/kenz-e9a7c.appspot.com/o/حصان1%20(1).gif?alt=media&token=21370ec0-8314-4d9c-a617-831518982c8d").into(binding.webss);


            adapterGifts = GiftAdapter(mutableListOf(
                Grid(R.raw.hor ,R.raw.hors_sound, "حصان" , "25 coins"),
                Grid(R.raw.camel_l,R.raw.camel , "جمل" , "15 coins"),
                Grid(R.raw.nakhlah_l,R.raw.nakhala , "نخلة" ,"45 coins"),
                Grid(R.raw.siaf_l,R.raw.saif, "سيف" , "25 coins"),

            ),object : onGiftclicks{
                override fun onUserClickOnGift(pos: Int) {

//dialog.hide()

//                    binding.giftItem.show()










                    if (pos ==0){
                        mediaPlayer = MediaPlayer.create(this@LiveEventActivity, R.raw.hors_sound)
                        binding.giftItem0.hide()
                        binding.webss.show()

                    }else if (pos == 1)

                    {

                        mediaPlayer = MediaPlayer.create(this@LiveEventActivity, R.raw.camel)
                        binding.giftItem1.show()

                    }
                    else if (pos == 2)
                    {
                        mediaPlayer = MediaPlayer.create(this@LiveEventActivity, R.raw.nakhala)
                        binding.giftItem2.show()


                    }else if (pos == 3)
                    {
                        mediaPlayer = MediaPlayer.create(this@LiveEventActivity, R.raw.saif)
                        binding.giftItem3.show()


                    }


                    mediaPlayer?.start()

                    dialog.dismiss()

                    Handler(Looper.getMainLooper()).postDelayed({
                        // This will be executed after 3 seconds
                        mediaPlayer?.stop()
                        binding.giftItem0.hide()
                        binding.giftItem1.hide()
                        binding.giftItem2.hide()
                        binding.giftItem3.hide()

                        binding.webss.hide()

                    }, 3000)  // Delay in milliseconds (3000ms = 3s)







                }
            })
            botBinding.gridRv.adapter = adapterGifts
            botBinding.gridRv.layoutManager = GridLayoutManager(this, 2 , GridLayoutManager.HORIZONTAL ,false)
            dialog.show()




        }

    }


    fun setUpRecyclerView(){


    }

    abstract fun customOnBackPressed()

    protected open fun attachToLiveEvent() {
        liveEvent ?: run {
            showToast("LiveEvent unavailable")
            return
        }
    }

    protected open fun initLiveEventView() {
        val optionVisibility = if (liveEvent?.isActiveHost == true) View.VISIBLE else View.GONE
        binding.ivMore.visibility = optionVisibility
        binding.ivSwitch.visibility = optionVisibility
        binding.ivMic.visibility = optionVisibility
        binding.ivVideo.visibility = optionVisibility
        binding.tvTimer.visibility = optionVisibility
        setLiveStateView(liveEvent?.state ?: LiveEventState.CREATED)
        if (liveEvent?.state == LiveEventState.ONGOING) {
            startTimer(liveEvent?.duration ?: 0L)
        }
        binding.viewSetting.setOnClickListener {
            if (binding.clSetting.isVisible) {
                binding.clSetting.visibility = GONE
                binding.viewOverlay.visibility = GONE
            } else {
                binding.clSetting.visibility = VISIBLE
                binding.viewOverlay.visibility = VISIBLE
            }
        }
        binding.tvParticipantCount.text = (liveEvent?.participantCount ?: 0).displayFormat()
        updateToolbarView()
    }

    protected fun updateToolbarView() {
        cachedCoverUrl = liveEvent?.coverUrl
//        binding.ivCover.load(liveEvent?.coverUrl) {
//            error(R.drawable.logo)
//        }
        binding.tvTitle.text = if (!liveEvent?.title.isNullOrEmpty()) liveEvent?.title else getString(R.string.live_event)
        binding.tvDescription.text = liveEvent?.hosts?.joinToString(", ") { it.userId } ?: ""
//        binding.tvDescription.text = liveEvent?.host?.userId ?: ""
    }

    protected fun initHostView() {
        val hosts = liveEvent?.hosts
        if (!hosts.isNullOrEmpty()) {
            adapter.addItems(hosts)
        }
//        val host = liveEvent?.host
//        if (host != null) {
//            adapter.addItems(listOf(host))
//        }
    }

    protected fun addHostVideoView(host: Host) {
        adapter.addItems(listOf(host))
    }


    protected fun removeHostVideoView(host: Host) {
        adapter.removeItems(listOf(host))
    }

    protected fun updateHostVideoView(host: Host) {
        adapter.updateItemView(host.hostId)
    }

    protected open fun finishLiveEvent(isEnded: Boolean = false) {
        stopTimer()
        if (isEnded) {
            val activity = if (liveEvent?.myRole == LiveEventRole.HOST) LiveEventSummaryActivity::class.java else LiveEventEndedActivity::class.java
            val intent = Intent(this, activity)
            intent.putExtra(INTENT_KEY_LIVE_EVENT_COVER_URL, cachedCoverUrl)
            intent.putExtra(INTENT_KEY_LIVE_EVENT_TOTAL_PARTICIPANTS, liveEvent?.cumulativeParticipantCount)
            intent.putExtra(INTENT_KEY_LIVE_EVENT_PEAK_PARTICIPANTS, liveEvent?.peakParticipantCount)
            intent.putExtra(INTENT_KEY_LIVE_EVENT_DURATION, liveEvent?.duration)
            startActivity(intent)
        }
        finish()
    }

    private fun getLiveEvent() {
        val liveEventId = liveEventId ?: run {
            finish()
            return
        }
        val liveEvent = SendbirdLive.getCachedLiveEvent(liveEventId) ?: run {
            finish()
            return
        }
        this.liveEvent = liveEvent
        adapter = HostAdapter(liveEvent)
        binding.rvLiveView.adapter = adapter
        binding.rvLiveView.layoutManager = GridLayoutManager(this, 2).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val listSize = adapter.itemCount
                    return when {
                        listSize <= 2 -> 2
                        listSize == 3 && position == 0 -> 2
                        listSize == 3 -> 1
                        listSize == 4 -> 1
                        listSize == 5 -> 1
                        else -> 1
                    }
                }
            }
        }

        liveEvent.addListener("${UUID.randomUUID()}", liveEventListenerImpl)
        attachToLiveEvent()
        initHostView()
        initLiveEventView()
    }

    protected fun distributeReactionAnimations(key: String, count: Int) {
        var increasedReactionCount = count - (reactionCountMap[key] ?: 0)
        if (increasedReactionCount < 0)
            return
        else if (increasedReactionCount > ReactionConstants.REACTION_MAXIMUM_COUNT)
            increasedReactionCount = ReactionConstants.REACTION_MAXIMUM_COUNT
        reactionCountMap[key] = count

        for(i in 0 until increasedReactionCount) {
            runOnSingleThreadPoolWithDelay(millisecond = (ReactionConstants.REACTION_SERVER_PUSH_INTERVAL / increasedReactionCount * i).toLong()) {
                runOnUiThread {
                    binding.lervLikeReaction.startAnimation()
                }
            }
        }
    }

    private fun runOnSingleThreadPoolWithDelay(millisecond: Long, runnable: Runnable) {
        if (!singleThreadExecutor.isShutdown) {
            singleThreadExecutor.schedule(runnable, millisecond, TimeUnit.MILLISECONDS)
        }
    }

    protected fun setLiveStateView(state: LiveEventState) {
        val (indicatorRes, stateName) = when(state) {
            LiveEventState.CREATED, LiveEventState.READY -> Pair(R.drawable.shape_live_event_pause_indicator, getString(R.string.open))
            LiveEventState.ONGOING ->  Pair(R.drawable.shape_live_event_ongoing_indicator, getString(R.string.live))
            LiveEventState.ENDED ->  Pair(R.drawable.shape_live_event_pause_indicator, getString(R.string.ended))
        }
//        binding.indicator.setBackgroundResource(indicatorRes)
//        binding.tvState.text = stateName
    }

    protected fun startTimer(baseTime: Long) {
        if (countUpTimer == null) {
            binding.tvTimer.visibility = View.VISIBLE
            binding.tvTimer.setTextAppearance(R.style.TimerButtonRed01Style)
            binding.tvTimer.setBackgroundResource(R.drawable.shape_timer_background_red)
            countUpTimer = CountUpTimer(baseTime) {
                runOnUiThread {
                    binding.tvTimer.text = it.toTimerFormat()
                }
            }.apply { start() }
        }
    }

    private fun stopTimer() {
        countUpTimer?.stop()
        countUpTimer = null
    }

    private fun initOpenChannelView() {
//        val liveEventId = liveEventId ?: return

//binding.reaction
        Log.d("ertyuiopdsaj",liveEventId.toString())
        supportFragmentManager.commit {
            replace(R.id.fcvChat, getOpenChannelFragment(liveEventId ?:"cc0fa9a4-b291-4d27-ace1-a81b92c1284e"))
        }
        openChannelFragment.title = liveEvent?.title
        openChannelFragment.profileImageUrl = liveEvent?.hosts?.first()?.profileURL ?: liveEvent?.coverUrl
        openChannelFragment.onHeaderChatButtonClickListener = View.OnClickListener { setChatViewVisibility(!openChannelFragment.isVisible) }
    }

    private fun setChatViewVisibility(isVisible: Boolean) {
        val (height, visibility, iconRes) =
            if (isVisible) Triple(374.dp, View.VISIBLE, R.drawable.icon_chat_hide)
            else Triple(ViewGroup.LayoutParams.WRAP_CONTENT, View.GONE, R.drawable.icon_chat_show)
        with(binding.fcvChat) {
            layoutParams.height = height
            requestLayout()
        }
        with(openChannelFragment) {
            openChannelListVisibility = visibility
            chatIconRes = iconRes
        }
    }

    abstract fun getOpenChannelFragment(liveEventId: String): OpenChannelFragment

    open class LiveEventListenerImpl : LiveEventListener {
        override fun onCustomItemsDelete(liveEvent: LiveEvent, customItems: Map<String, String>, deletedKeys: List<String>) {}
        override fun onCustomItemsUpdate(liveEvent: LiveEvent, customItems: Map<String, String>, updatedKeys: List<String>) {}
        override fun onDisconnected(liveEvent: LiveEvent, e: SendbirdException) {}
        override fun onExited(liveEvent: LiveEvent, e: SendbirdException) {}

        override fun onHostConnected(liveEvent: LiveEvent, host: Host) {}
        override fun onHostDisconnected(liveEvent: LiveEvent, host: Host) {}
        override fun onHostEntered(liveEvent: LiveEvent, host: Host) {}
        override fun onHostExited(liveEvent: LiveEvent, host: Host) {}
        override fun onHostMuteAudio(liveEvent: LiveEvent, host: Host) {}
        override fun onHostStartVideo(liveEvent: LiveEvent, host: Host) {}
        override fun onHostStopVideo(liveEvent: LiveEvent, host: Host) {}
        override fun onHostUnmuteAudio(liveEvent: LiveEvent, host: Host) {}
        override fun onLiveEventEnded(liveEvent: LiveEvent) {}
        override fun onLiveEventInfoUpdated(liveEvent: LiveEvent) {}
        override fun onLiveEventReady(liveEvent: LiveEvent) {}
        override fun onLiveEventStarted(liveEvent: LiveEvent) {}
        override fun onParticipantCountChanged(liveEvent: LiveEvent, participantCountInfo: ParticipantCountInfo) {}
        override fun onReactionCountUpdated(liveEvent: LiveEvent, key: String, count: Int) {}
        override fun onReconnected(liveEvent: LiveEvent) {}

    }
    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources
        mediaPlayer?.release()
    }

}