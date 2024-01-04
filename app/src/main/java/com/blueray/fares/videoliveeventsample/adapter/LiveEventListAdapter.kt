package com.sendbird.live.videoliveeventsample.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.blueray.fares.R
import com.blueray.fares.databinding.ListItemLiveEventBinding
import com.blueray.fares.videoliveeventsample.adapter.HostAdapter
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.sendbird.live.LiveEvent
import com.sendbird.live.LiveEventState

import com.blueray.fares.videoliveeventsample.util.OnItemClickListener
import com.blueray.fares.videoliveeventsample.util.displayFormat
import com.blueray.fares.videoliveeventsample.util.showToast
import com.blueray.fares.videoliveeventsample.view.LiveEventActivity
import com.blueray.fares.videoliveeventsample.view.LiveEventForParticipantActivity
import com.sendbird.live.Host
import com.sendbird.live.ParticipantCountInfo
import com.sendbird.live.SendbirdLive
import com.sendbird.webrtc.SendbirdException
import java.util.UUID

open class LiveEventListAdapter : RecyclerView.Adapter<LiveEventListAdapter.LiveEventListHolder>() {
    private val liveEventList = mutableListOf<LiveEvent>()
    private val cachedLiveEventInfoList = mutableListOf<LiveEventInfo>()
    var onItemClickListener: OnItemClickListener<LiveEvent>? = null
    var emptyStateView: View? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LiveEventListHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemLiveEventBinding = ListItemLiveEventBinding.inflate(inflater, parent, false)
        binding.pulsator2.start()
        return LiveEventListHolder(binding)
    }

    override fun onBindViewHolder(holder: LiveEventListHolder, position: Int) {
        val liveEvent = liveEventList[position]
        holder.bind(liveEvent)
        holder.itemView.setOnClickListener {
            val channelPosition = holder.adapterPosition
            if (channelPosition != RecyclerView.NO_POSITION && onItemClickListener != null) {
                onItemClickListener?.onItemClick(it, channelPosition, liveEventList[channelPosition])
            }
        }
    }

    override fun getItemCount() = liveEventList.size

    fun addItems(liveEvents: List<LiveEvent>?) {
        if (liveEvents != null) {
            val list = mutableListOf<LiveEvent>().apply {
                addAll(liveEventList)
                addAll(liveEvents)
            }
            submitList(list.toList())
        }
    }

    fun submitList(liveEvents: List<LiveEvent>) {
        val diffCallback = LiveEventDiffCallback(cachedLiveEventInfoList, liveEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        cachedLiveEventInfoList.clear()
        cachedLiveEventInfoList.addAll(LiveEventInfo.toLiveEventInfoList(liveEvents))
        liveEventList.clear()
        liveEventList.addAll(liveEvents)
        diffResult.dispatchUpdatesTo(this)
        checkEmpty()
    }

    private fun checkEmpty() {
        val visibility = if (liveEventList.isEmpty()) View.VISIBLE else View.GONE
        emptyStateView?.visibility = visibility
    }

    class LiveEventListHolder(private val binding: ListItemLiveEventBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(liveEvent: LiveEvent) {
            binding.tvTitle.text = if (!liveEvent.title.isNullOrEmpty()) liveEvent.title else binding.root.context.getString(R.string.live_event)
//            binding.tvSubtitle.text = liveEvent.hosts?.hostId
            binding.tvParticipantCount.text = liveEvent.participantCount.displayFormat()
            val indicatorBackgroundRes = if (liveEvent.state == LiveEventState.ONGOING) R.drawable.shape_live_event_ongoing_indicator else R.drawable.shape_live_event_pause_indicator
            binding.vLiveIndicator.setBackgroundResource(indicatorBackgroundRes)

            val coverBackgroundRes =
                if (!liveEvent.coverUrl.isNullOrBlank()) liveEvent.coverUrl
                else when (adapterPosition % 5) {
                    0 -> R.drawable.coverimage_live_1
                    1 -> R.drawable.coverimage_live_2
                    2 -> R.drawable.coverimage_live_3
                    3 -> R.drawable.coverimage_live_4
                    else -> R.drawable.coverimage_live_5
                }
            binding.ivLiveThumbnail.load(coverBackgroundRes) {
                crossfade(true)
                placeholder(R.drawable.logo2)
                error(R.drawable.logo2)
            }
            val (stringResId, textAppearance, backgroundResId) = when (liveEvent.state) {
                LiveEventState.CREATED -> Triple(R.string.upcoming, R.style.Text12Primary300Bold, R.drawable.shape_live_state_created_background)
                LiveEventState.READY -> Triple(R.string.open, R.style.Text12Open300Bold, R.drawable.shape_live_state_ready_background)
                LiveEventState.ONGOING -> Triple(R.string.live, R.style.Text12OnDark01Bold, R.drawable.shape_live_state_ongoing_background)
                LiveEventState.ENDED -> Triple(R.string.ended, R.style.Text12OnLight02Bold, R.drawable.shape_live_state_ended_background)
            }
//            with(binding.tvLiveEventStatus) {
//                this.text = context.getString(stringResId)
//                setTextAppearance(textAppearance)
//                setBackgroundResource(backgroundResId)
//            }
        }
    }

    private class LiveEventDiffCallback(
        private val oldItems: List<LiveEventInfo>,
        private val newItems: List<LiveEvent>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItems.size
        }

        override fun getNewListSize(): Int {
            return newItems.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.liveEventId == newItem.liveEventId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return oldItem.state == newItem.state
                    && oldItem.title == newItem.title
                    && oldItem.coverUrl == newItem.coverUrl
//                    && oldItem.hostName == newItem.hosts.joinToString(", ") { it.userId }
                    && oldItem.isHostStreaming == newItem.isHostStreaming
                    && oldItem.participantCount == newItem.participantCount
        }
    }

    internal class LiveEventInfo(val liveEvent: LiveEvent) {
        val liveEventId: String = liveEvent.liveEventId
        val participantCount: Int = liveEvent.participantCount
        val title: String? = liveEvent.title
        //        val hostName: String? = liveEvent.hosts.joinToString(", ") { it.userId }
        val state: LiveEventState = liveEvent.state
        val isHostStreaming: Boolean = liveEvent.isHostStreaming
        val coverUrl: String? = liveEvent.coverUrl

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as LiveEventInfo
            if (liveEvent != other.liveEvent) return false
            if (liveEventId != other.liveEventId) return false
            if (participantCount != other.participantCount) return false
            if (title != other.title) return false
//            if (hostName != other.hostName) return false
            if (state != other.state) return false
            if (isHostStreaming != other.isHostStreaming) return false
            if (coverUrl != other.coverUrl) return false
            return true
        }

        override fun hashCode(): Int {
            var result = liveEvent.hashCode()
            result = 31 * result + liveEventId.hashCode()
            result = 31 * result + participantCount
            result = 31 * result + title.hashCode()
//            result = 31 * result + (hostName?.hashCode() ?: 0)
            result = 31 * result + state.hashCode()
            result = 31 * result + isHostStreaming.hashCode()
            result = 31 * result + coverUrl.hashCode()
            return result
        }

        companion object {
            fun toLiveEventInfoList(liveEventList: List<LiveEvent>): List<LiveEventInfo> {
                val results: MutableList<LiveEventInfo> = ArrayList()
                for (liveEvent in liveEventList) {
                    results.add(LiveEventInfo(liveEvent))
                }
                return results
            }
        }
    }
}


//open class LiveEventListAdapter : RecyclerView.Adapter<LiveEventListAdapter.LiveEventListHolder>() {
//    private val liveEventList = mutableListOf<LiveEvent>()
//    private val cachedLiveEventInfoList = mutableListOf<LiveEventInfo>()
//    var onItemClickListener: OnItemClickListener<LiveEvent>? = null
//    var emptyStateView: View? = null
//     lateinit var liveEventListenerImpl: LiveEventActivity.LiveEventListenerImpl
//
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): LiveEventListHolder {
//        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
//        val binding: ListItemLiveEventBinding = ListItemLiveEventBinding.inflate(inflater, parent, false)
//        return LiveEventListHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: LiveEventListHolder, position: Int) {
//        val liveEvent = liveEventList[position]
//        holder.bind(liveEvent)
//        holder.itemView.setOnClickListener {
//            val channelPosition = holder.adapterPosition
//            if (channelPosition != RecyclerView.NO_POSITION && onItemClickListener != null) {
//                onItemClickListener?.onItemClick(it, channelPosition, liveEventList[channelPosition])
//            }
//        }
//        liveEvent.addListener("${UUID.randomUUID()}", liveEventListenerImpl)
//        liveEvent ?: run {
////            showToast("LiveEvent unavailable")
//            return
//        }
////        initHostView()
////        initLiveEventView()
//    }
////    protected open fun attachToLiveEvent() {
////        liveEvent ?: run {
////            showToast("LiveEvent unavailable")
////            return
////        }
////    }
//
//
//    override fun getItemCount() = liveEventList.size
//
//    fun addItems(liveEvents: List<LiveEvent>?) {
//        if (liveEvents != null) {
//            val list = mutableListOf<LiveEvent>().apply {
//                addAll(liveEventList)
//                addAll(liveEvents)
//            }
//            submitList(list.toList())
//        }
//    }
//
//    fun submitList(liveEvents: List<LiveEvent>) {
//        val diffCallback = LiveEventDiffCallback(cachedLiveEventInfoList, liveEvents)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        cachedLiveEventInfoList.clear()
//        cachedLiveEventInfoList.addAll(LiveEventInfo.toLiveEventInfoList(liveEvents))
//        liveEventList.clear()
//        liveEventList.addAll(liveEvents)
//        diffResult.dispatchUpdatesTo(this)
//        checkEmpty()
//    }
//
//    private fun checkEmpty() {
//        val visibility = if (liveEventList.isEmpty()) View.VISIBLE else View.GONE
//        emptyStateView?.visibility = visibility
//    }
//
//    class LiveEventListHolder(private val binding: ListItemLiveEventBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//
//        fun bind(liveEvent: LiveEvent) {
//            binding.tvTitle.text = if (!liveEvent.title.isNullOrEmpty()) liveEvent.title else binding.root.context.getString(R.string.live_event)
////            binding.tvSubtitle.text = liveEvent.hosts.joinToString(", ") { it.userId }
//            binding.tvParticipantCount.text = liveEvent.participantCount.displayFormat()
//            val indicatorBackgroundRes = if (liveEvent.state == LiveEventState.ONGOING) R.drawable.shape_live_event_ongoing_indicator else R.drawable.shape_live_event_pause_indicator
////            binding.vLiveIndicator.setBackgroundResource(indicatorBackgroundRes)
//
//            val coverBackgroundRes =
//                if (!liveEvent.coverUrl.isNullOrBlank()) liveEvent.coverUrl
//                else when (adapterPosition % 5) {
//                    0 -> R.drawable.coverimage_live_1
//                    1 -> R.drawable.coverimage_live_2
//                    2 -> R.drawable.coverimage_live_3
//                    3 -> R.drawable.coverimage_live_4
//                    else -> R.drawable.coverimage_live_5
//                }
////            binding.ivLiveThumbnail.load(coverBackgroundRes) {
////                crossfade(true)
////                placeholder(R.drawable.icon_default_user)
////                error(R.drawable.icon_default_user)
////            }
//            val (stringResId, textAppearance, backgroundResId) = when (liveEvent.state) {
//                LiveEventState.CREATED -> Triple(R.string.upcoming, R.style.Text12Primary300Bold, R.drawable.shape_live_state_created_background)
//                LiveEventState.READY -> Triple(R.string.open, R.style.Text12Open300Bold, R.drawable.shape_live_state_ready_background)
//                LiveEventState.ONGOING -> Triple(R.string.live, R.style.Text12OnDark01Bold, R.drawable.shape_live_state_ongoing_background)
//                LiveEventState.ENDED -> Triple(R.string.ended, R.style.Text12OnLight02Bold, R.drawable.shape_live_state_ended_background)
//            }
////            with(binding.tvLiveEventStatus) {
////                this.text = context.getString(stringResId)
////                setTextAppearance(textAppearance)
////                setBackgroundResource(backgroundResId)
////            }
//        }
//    }
//
//    private class LiveEventDiffCallback(
//        private val oldItems: List<LiveEventInfo>,
//        private val newItems: List<LiveEvent>
//    ) : DiffUtil.Callback() {
//        override fun getOldListSize(): Int {
//            return oldItems.size
//        }
//
//        override fun getNewListSize(): Int {
//            return newItems.size
//        }
//
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem.liveEventId == newItem.liveEventId
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            val oldItem = oldItems[oldItemPosition]
//            val newItem = newItems[newItemPosition]
//            return oldItem.state == newItem.state
//                    && oldItem.title == newItem.title
//                    && oldItem.coverUrl == newItem.coverUrl
////                    && oldItem.hostName == newItem.hosts.joinToString(", ") { it.userId }
//                    && oldItem.isHostStreaming == newItem.isHostStreaming
//                    && oldItem.participantCount == newItem.participantCount
//        }
//    }
//
//    internal class LiveEventInfo(val liveEvent: LiveEvent) {
//        val liveEventId: String = liveEvent.liveEventId
//        val participantCount: Int = liveEvent.participantCount
//        val title: String? = liveEvent.title
////        val hostName: String? = liveEvent.hosts.joinToString(", ") { it.userId }
//        val state: LiveEventState = liveEvent.state
//        val isHostStreaming: Boolean = liveEvent.isHostStreaming
//        val coverUrl: String? = liveEvent.coverUrl
//
//        override fun equals(other: Any?): Boolean {
//            if (this === other) return true
//            if (javaClass != other?.javaClass) return false
//            other as LiveEventInfo
//            if (liveEvent != other.liveEvent) return false
//            if (liveEventId != other.liveEventId) return false
//            if (participantCount != other.participantCount) return false
//            if (title != other.title) return false
////            if (hostName != other.hostName) return false
//            if (state != other.state) return false
//            if (isHostStreaming != other.isHostStreaming) return false
//            if (coverUrl != other.coverUrl) return false
//            return true
//        }
//
//        override fun hashCode(): Int {
//            var result = liveEvent.hashCode()
//            result = 31 * result + liveEventId.hashCode()
//            result = 31 * result + participantCount
//            result = 31 * result + title.hashCode()
////            result = 31 * result + (hostName?.hashCode() ?: 0)
//            result = 31 * result + state.hashCode()
//            result = 31 * result + isHostStreaming.hashCode()
//            result = 31 * result + coverUrl.hashCode()
//            return result
//        }
//
//        companion object {
//            fun toLiveEventInfoList(liveEventList: List<LiveEvent>): List<LiveEventInfo> {
//                val results: MutableList<LiveEventInfo> = ArrayList()
//                for (liveEvent in liveEventList) {
//                    results.add(LiveEventInfo(liveEvent))
//                }
//                return results
//            }
//        }
//    }
//     var liveEventListenerImpl = object : LiveEventActivity.LiveEventListenerImpl() {
//        override fun onDisconnected(liveEvent: LiveEvent, e: SendbirdException) {
//            finish()
//        }
//        override fun onHostConnected(liveEvent: LiveEvent, host: Host) {
//            showBanner(null)
//        }
//        override fun onHostDisconnected(liveEvent: LiveEvent, host: Host) {
//            showBanner(getString(R.string.banner_message_host_disconnected))
//        }
//        override fun onHostEntered(liveEvent: LiveEvent, host: Host) {
//            updateToolbarView()
//            addHostVideoView(host)
//        }
//        override fun onHostExited(liveEvent: LiveEvent, host: Host) {
//            updateToolbarView()
//            removeHostVideoView(host)
//        }
//        override fun onHostStartVideo(liveEvent: LiveEvent, host: Host) {
//            updateHostVideoView(host)
//        }
//        override fun onHostStopVideo(liveEvent: LiveEvent, host: Host) {
//            updateHostVideoView(host)
//        }
//        override fun onHostMuteAudio(liveEvent: LiveEvent, host: Host) {
//            updateHostVideoView(host)
//        }
//        override fun onHostUnmuteAudio(liveEvent: LiveEvent, host: Host) {
//            updateHostVideoView(host)
//        }
//        override fun onLiveEventEnded(liveEvent: LiveEvent) {
//            finishLiveEvent(true)
//        }
//        override fun onLiveEventStarted(liveEvent: LiveEvent) {
//            showBanner(null)
//            startTimer(0L)
//            setLiveStateView(liveEvent.state)
//            binding.tvParticipantCount.text = liveEvent.participantCount.displayFormat()
//        }
//
//        override fun onParticipantCountChanged(liveEvent: LiveEvent, participantCountInfo: ParticipantCountInfo) {
//            binding.tvParticipantCount.text = liveEvent.participantCount.displayFormat()
//        }
//
//        override fun onReactionCountUpdated(liveEvent: LiveEvent, key: String, count: Int) {
//        }
//    }
//
//}
//class LiveEventListAdapter(private val clickListener: (LiveEvent) -> Unit) : RecyclerView.Adapter<LiveEventListAdapter.LiveEventViewHolder>() {
//    private val liveEventList = mutableListOf<LiveEvent>()
//
//    var onItemClickListener: OnItemClickListener<LiveEvent>? = null
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveEventViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = ListItemLiveEventBinding.inflate(inflater, parent, false)
//        return LiveEventViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: LiveEventViewHolder, position: Int) {
//        holder.bind(liveEventList[position])
//        holder.itemView.setOnClickListener {
//            onItemClickListener?.onItemClick(it, position, liveEventList[position])
//        }
//        val liveEvent = liveEventList[position]
//        holder.itemView.setOnClickListener {
//            clickListener(liveEvent)
//            Log.d("ertyuiop","drfytgh")
//        }
////        SendbirdLive.getCachedLiveEvent(liveEvent.liveEventId)
////            ?: run {
//////            finish()
////                Log.d("testst","errr")
////                return
////            }
//
//    }
//
//    override fun getItemCount() = liveEventList.size
//
//    fun submitList(liveEvents: List<LiveEvent>) {
//        liveEventList.clear()
//        liveEventList.addAll(liveEvents)
//        notifyDataSetChanged()
//    }
//
//    class LiveEventViewHolder(private val binding: ListItemLiveEventBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(liveEvent: LiveEvent) {
//
////            binding.tvTitle.text = liveEvent.title ?: "Live Event"
////            binding.tvParticipantCount.text = liveEvent.participantCount.toString()
////            binding.ivLiveThumbnail.load(liveEvent.coverUrl) {
////                crossfade(true)
////                placeholder(R.drawable.placeholder_image)
////                error(R.drawable.error_image)
////            }
//
//            val eventState = when (liveEvent.state) {
//                LiveEventState.CREATED -> "Created"
//                LiveEventState.READY -> "Ready"
//                LiveEventState.ONGOING -> "Live"
//                LiveEventState.ENDED -> "Ended"
//                else -> "Unknown State"
//            }
////            binding.tvTitle.text = eventState
//        }
//    }
//        fun addItems(liveEvents: List<LiveEvent>?) {
//        if (liveEvents != null) {
//            val list = mutableListOf<LiveEvent>().apply {
//                addAll(liveEventList)
//                addAll(liveEvents)
//            }
//            submitList(list.toList())
//        }
//    }
////    private fun getLiveEvent(pos:Int) {
////        val liveEventId = liveEventList[pos].liveEventId ?: run {
////            Log.d("ERRRRRorPlay","")
////            return
////        }
////        val liveEvent = SendbirdLive.getCachedLiveEvent(liveEventId) ?: run {
////            return
////        }
////      liveEvent
////        adapter = HostAdapter(liveEvent)
////        binding.rvLiveView.adapter = adapter
////        binding.rvLiveView.layoutManager = GridLayoutManager(this, 2).apply {
////            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
////                override fun getSpanSize(position: Int): Int {
////                    val listSize = adapter.itemCount
////                    return when {
////                        listSize <= 2 -> 2
////                        listSize == 3 && position == 0 -> 2
////                        listSize == 3 -> 1
////                        listSize == 4 -> 1
////                        listSize == 5 -> 1
////                        else -> 1
////                    }
////                }
////            }
////        }
////
////        liveEvent.addListener("${UUID.randomUUID()}", liveEventListenerImpl)
////        attachToLiveEvent()
////        initHostView()
////        initLiveEventView()
////    }
//
//}
