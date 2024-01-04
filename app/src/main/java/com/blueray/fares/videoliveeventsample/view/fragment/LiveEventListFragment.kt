package com.blueray.fares.videoliveeventsample.view.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.blueray.fares.R
import com.blueray.fares.databinding.FragmentLiveEventListBinding
import com.blueray.fares.helpers.ViewUtils.hide
import com.blueray.fares.helpers.ViewUtils.show
import com.blueray.fares.videoliveeventsample.adapter.HostAdapter
import com.sendbird.live.LiveEvent
import com.sendbird.live.LiveEventListQuery
import com.sendbird.live.LiveEventRole
import com.sendbird.live.LiveEventState
import com.sendbird.live.SendbirdLive
import com.sendbird.live.videoliveeventsample.adapter.LiveEventListAdapter
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_CREATED_LIVE_EVENT_ID
import com.blueray.fares.videoliveeventsample.util.INTENT_KEY_LIVE_EVENT_ID
import com.blueray.fares.videoliveeventsample.util.OnItemClickListener
import com.blueray.fares.videoliveeventsample.util.areAnyPermissionsGranted
import com.blueray.fares.videoliveeventsample.util.showAlertDialog
import com.blueray.fares.videoliveeventsample.util.showListDialog
import com.blueray.fares.videoliveeventsample.util.showPermissionDenyDialog
import com.blueray.fares.videoliveeventsample.util.showToast
import com.blueray.fares.videoliveeventsample.view.CreateLiveEventActivity
import com.blueray.fares.videoliveeventsample.view.LiveEventForParticipantActivity
import com.blueray.fares.videoliveeventsample.view.LiveEventSetUpActivity
import com.sendbird.live.Host
import com.sendbird.live.LiveEventListener
import com.sendbird.live.ParticipantCountInfo
import com.sendbird.webrtc.SendbirdException
import java.util.UUID

class LiveEventListFragment :
    BaseFragment<FragmentLiveEventListBinding>(FragmentLiveEventListBinding::inflate) {

    private var liveEventListQuery: LiveEventListQuery? = null
    private val params = LiveEventListQuery.Params()
    private val permissions = mutableListOf(
        Manifest.permission.BLUETOOTH
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addAll(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.all { permission -> permission.value }) {
            requireContext().showToast(R.string.permission_dialog_granted)
        } else {
            requireContext().showPermissionDenyDialog(
                true,
                it.filter { permission -> !permission.value }.keys.toList()
            )
        }
    }
    private var createLiveEventResult : ActivityResultLauncher<Intent>? = null
    lateinit var adapter: LiveEventListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLiveEventResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val liveEventId = result.data?.getStringExtra(INTENT_KEY_CREATED_LIVE_EVENT_ID) ?: return@registerForActivityResult
                val createdLiveEvent = SendbirdLive.getCachedLiveEvent(liveEventId) ?: return@registerForActivityResult
                adapter.addItems(listOf(createdLiveEvent))
            }
        }
        initLiveEventListView()
        initHeaderView()
        val mSnapHelper: SnapHelper = PagerSnapHelper()
        mSnapHelper.attachToRecyclerView(binding.rvLiveEvents)
    }

    private fun initHeaderView() {
        binding.tvCreateLiveEvent.setOnClickListener {
            createLiveEvent()
        }
    }

    private fun createLiveEvent() {
        val intent = Intent(requireContext(), CreateLiveEventActivity::class.java)
        startActivity(intent)
    }

    private fun initLiveEventListView() {
        adapter = LiveEventListAdapter()
        liveEventListQuery = SendbirdLive.createLiveEventListQuery(params)
        binding.rvLiveEvents.adapter = adapter
        adapter.onItemClickListener = OnItemClickListener { _, position, liveEvent ->
            getLiveEvent(liveEvent.liveEventId) { newLiveEvent ->
                if (position != -1) adapter.notifyItemChanged(position)
//                if (newLiveEvent.myRole == LiveEventRole.HOST) {
//                    requireActivity().showListDialog(
//                        title = getString(R.string.dialog_message_choose_your_role),
//                        listItem = listOf(getString(R.string.hosts), getString(R.string.participant))
//                    ) { _, position ->
//                        val role = if (position == 0) LiveEventRole.HOST else LiveEventRole.PARTICIPANT
//                        enterTheLiveEvent(newLiveEvent, role)
//                    }
//                } else {
//                }

                binding.progressBar.show()
                enterTheLiveEvent(newLiveEvent, LiveEventRole.PARTICIPANT)

            }
        }
        adapter.emptyStateView = binding.clEmpty
        binding.rvLiveEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    loadNext()
                }
            }
        })
        binding.srlLiveEvent.setOnRefreshListener {
            liveEventListQuery = SendbirdLive.createLiveEventListQuery(params)
            loadNext(true)
        }
        loadNext()
    }

    private fun enterAsHost(liveEvent: LiveEvent) {
        val requestPermissions = permissions.toMutableList().apply {
            addAll(
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_PHONE_STATE
                )
            )
        }.toTypedArray()
        if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
            requestPermissionLauncher.launch(requestPermissions)
            return
        }
        val intent = Intent(requireContext(), LiveEventSetUpActivity::class.java)
        intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
        requireContext().startActivity(intent)
    }

    private fun enterAsParticipant(liveEvent: LiveEvent) {
        binding.progressBar.hide()
        when (liveEvent.state) {
            LiveEventState.CREATED -> {
                requireActivity().showAlertDialog(
                    message = getString(R.string.error_message_created_enter_dialog_description),
                    posText = getString(R.string.okay)
                )
                return
            }
            LiveEventState.READY, LiveEventState.ONGOING -> {
                val requestPermissions = permissions.toTypedArray()
                if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
                    requestPermissionLauncher.launch(requestPermissions)
                    return
                }
                liveEvent.enter { e ->
                    if (e != null) {
                        requireActivity().showToast(e.message ?: "")
                        return@enter
                    }
                    val intent = Intent(requireContext(), LiveEventForParticipantActivity::class.java)
                    intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
                    requireContext().startActivity(intent)
                }
            }
            else -> {
            }
        }
    }

    private fun enterTheLiveEvent(liveEvent: LiveEvent, role: LiveEventRole) {
        if (liveEvent.state == LiveEventState.ENDED) {
            requireActivity().showAlertDialog(
                message = getString(R.string.error_message_ended_enter_dialog_description),
                posText = getString(R.string.okay)
            )
            return
        }

        if (role == LiveEventRole.HOST) {
            enterAsHost(liveEvent)
        } else {
            enterAsParticipant(liveEvent)
        }
    }

    private fun loadNext(isRefresh: Boolean = false) {
        if (liveEventListQuery?.hasNext == true) {
            liveEventListQuery?.next { list, e ->
                if (isRefresh) {
                    binding.srlLiveEvent.isRefreshing = false
                    binding.rvLiveEvents.scrollToPosition(0)
                }
                if (e != null) {
                    requireContext().showToast(e.message ?: "")
                    return@next
                }
                if (isRefresh) {
                    adapter.submitList(list ?: emptyList())
                } else {
                    adapter.addItems(list)
                }
            }
            return
        }
        if (isRefresh) binding.srlLiveEvent.isRefreshing = false
    }

    private fun getLiveEvent(liveEventId: String, callback: (LiveEvent) -> Unit) {
        SendbirdLive.getLiveEvent(liveEventId) getLiveEventLabel@{ liveEvent, e ->
            if (e != null || liveEvent == null) {
                requireContext().showToast(e?.message ?: "")
                return@getLiveEventLabel
            }
            callback.invoke(liveEvent)
        }
    }

}



//class LiveEventListFragment :
//    BaseFragment<FragmentLiveEventListBinding>(FragmentLiveEventListBinding::inflate){
//
//    private var liveEventListQuery: LiveEventListQuery? = null
//    private val params = LiveEventListQuery.Params()
//    private val permissions = mutableListOf(
//        Manifest.permission.BLUETOOTH
//    ).apply {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            addAll(
//                arrayOf(
//                    Manifest.permission.BLUETOOTH_CONNECT,
//                    Manifest.permission.BLUETOOTH_SCAN
//                )
//            )
//        }
//    }
//    private val requestPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) {
//        if (it.all { permission -> permission.value }) {
//            requireContext().showToast(R.string.permission_dialog_granted)
//        } else {
//            requireContext().showPermissionDenyDialog(
//                true,
//                it.filter { permission -> !permission.value }.keys.toList()
//            )
//        }
//    }
//    private var createLiveEventResult : ActivityResultLauncher<Intent>? = null
//    lateinit var adapter: LiveEventListAdapter
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        createLiveEventResult = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val liveEventId = result.data?.getStringExtra(INTENT_KEY_CREATED_LIVE_EVENT_ID) ?: return@registerForActivityResult
//                val createdLiveEvent = SendbirdLive.getCachedLiveEvent(liveEventId) ?: return@registerForActivityResult
//                adapter.addItems(listOf(createdLiveEvent))
//            }
//        }
////        initLiveEventListView()
////        initHeaderView()
//
////        setupCreateLiveEventResultListener()
//        initLiveEventListView()
//        setupSnapHelper()
//        setupHeaderView()
//
////        val mSnapHelper: SnapHelper = PagerSnapHelper()
////        mSnapHelper.attachToRecyclerView(binding.rvLiveEvents)
//    }
//    private fun setupCreateLiveEventResultListener() {
//        createLiveEventResult = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result ->
//            // ... existing logic ...
//        }
//    }
//    private fun setupHeaderView() {
//        binding.tvCreateLiveEvent.setOnClickListener {
//            createLiveEvent()
//        }
//    }
//
//    private fun setupSnapHelper() {
//        val mSnapHelper: SnapHelper = PagerSnapHelper()
//        mSnapHelper.attachToRecyclerView(binding.rvLiveEvents)
//    }
//
//    private fun initHeaderView() {
//        binding.tvCreateLiveEvent.setOnClickListener {
//            createLiveEvent()
//        }
//    }
//
//    private fun createLiveEvent() {
//        val intent = Intent(requireContext(), CreateLiveEventActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun initLiveEventListView() {
//         adapter = LiveEventListAdapter { liveEventId ->
//            getLiveEvent(liveEventId)
//        }
//        binding.rvLiveEvents.adapter = adapter
//
//        liveEventListQuery = SendbirdLive.createLiveEventListQuery(params)
//        adapter.onItemClickListener = OnItemClickListener { _, position, liveEvent ->
//            getLiveEvent(liveEvent.liveEventId) { newLiveEvent ->
////                if (position != -1) adapter.notifyItemChanged(position)
////                if (newLiveEvent.myRole == LiveEventRole.HOST) {
////                    requireActivity().showListDialog(
////                        title = getString(R.string.dialog_message_choose_your_role),
////                        listItem = listOf(getString(R.string.hosts), getString(R.string.participant))
////                    ) { _, position ->
////                        val role = if (position == 0) LiveEventRole.HOST else LiveEventRole.PARTICIPANT
////                        enterTheLiveEvent(newLiveEvent, role)
////                    }
////                } else {
////                    enterTheLiveEvent(newLiveEvent, LiveEventRole.PARTICIPANT)
////                }
//            }
//        }
////        adapter.emptyStateView = binding.clEmpty
//        binding.rvLiveEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (!recyclerView.canScrollVertically(1)) {
//                    loadNext()
//                }
//            }
//        })
//        binding.srlLiveEvent.setOnRefreshListener {
//            liveEventListQuery = SendbirdLive.createLiveEventListQuery(params)
//            loadNext(true)
//        }
//        loadNext()
//    }
//
//    private fun enterAsHost(liveEvent: LiveEvent) {
//        val requestPermissions = permissions.toMutableList().apply {
//            addAll(
//                arrayOf(
//                    Manifest.permission.RECORD_AUDIO,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_PHONE_STATE
//                )
//            )
//        }.toTypedArray()
//        if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
//            requestPermissionLauncher.launch(requestPermissions)
//            return
//        }
//        val intent = Intent(requireContext(), LiveEventSetUpActivity::class.java)
//        intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
//        requireContext().startActivity(intent)
//    }
//
//    private fun enterAsParticipant(liveEvent: LiveEvent) {
//        when (liveEvent.state) {
//            LiveEventState.CREATED -> {
//                requireActivity().showAlertDialog(
//                    message = getString(R.string.error_message_created_enter_dialog_description),
//                    posText = getString(R.string.okay)
//                )
//                return
//            }
//            LiveEventState.READY, LiveEventState.ONGOING -> {
//                val requestPermissions = permissions.toTypedArray()
//                if (!requireContext().areAnyPermissionsGranted(requestPermissions)) {
//                    requestPermissionLauncher.launch(requestPermissions)
//                    return
//                }
//
//                liveEvent.enter { e ->
//                    if (e != null) {
//                        requireActivity().showToast(e.message ?: "")
//                        return@enter
//                    }
//                    val intent = Intent(requireContext(), LiveEventForParticipantActivity::class.java)
//                    intent.putExtra(INTENT_KEY_LIVE_EVENT_ID, liveEvent.liveEventId)
//                    requireContext().startActivity(intent)
//                }
//            }
//            else -> {
//            }
//        }
//    }
//
//    private fun enterTheLiveEvent(liveEvent: LiveEvent, role: LiveEventRole) {
//        if (liveEvent.state == LiveEventState.ENDED) {
//            requireActivity().showAlertDialog(
//                message = getString(R.string.error_message_ended_enter_dialog_description),
//                posText = getString(R.string.okay)
//            )
//            return
//        }
//
//        if (role == LiveEventRole.HOST) {
//            enterAsHost(liveEvent)
//        } else {
//            enterAsParticipant(liveEvent)
//        }
//    }
//
//
//    private fun loadNext(isRefresh: Boolean = false) {
//        if (liveEventListQuery?.hasNext == true || isRefresh) {
//            liveEventListQuery?.next { list, e ->
//                if (isRefresh) {
//                    binding.srlLiveEvent.isRefreshing = false
//                    binding.rvLiveEvents.scrollToPosition(0)
//                }
//                if (e != null) {
//                    requireContext().showToast(e.message ?: "")
//                    return@next
//                }
//
//                val filteredList = list?.filter { it.state == LiveEventState.READY }
//                if (isRefresh) {
//                    adapter.submitList(filteredList ?: emptyList())
//                } else {
//                    adapter.submitList(filteredList?: emptyList())
//                }
//            }
//        } else if (isRefresh) {
//            binding.srlLiveEvent.isRefreshing = false
//        }
//    }
//
//    private fun getLiveEvent(liveEventId: String, callback: (LiveEvent) -> Unit) {
//        SendbirdLive.getLiveEvent(liveEventId) getLiveEventLabel@{ liveEvent, e ->
//            if (e != null || liveEvent == null) {
//                requireContext().showToast(e?.message ?: "")
//                return@getLiveEventLabel
//            }
//            callback.invoke(liveEvent)
//        }
//    }
//    protected open fun attachToLiveEvent(liveEvent:LiveEvent) {
//        liveEvent ?: run {
////            showToast("LiveEvent unavailable")
//            return
//        }
//    }
//    private fun getLiveEvent(liveEvent: LiveEvent) {
//        liveEvent ?: run {
////            showToast("LiveEvent unavailable")
//            return
//        }
//        attachToLiveEvent(liveEvent)
//        SendbirdLive.getCachedLiveEvent(liveEvent.liveEventId)
//            ?: run {
////            finish()
//            Log.d("testst","errr")
//            return
//        }
////        binding.rvLiveEvents.adapter = HostAdapter(liveEvent)
////        binding.rvLiveEvents.adapter = adapter
////        binding.rvLiveEvents.layoutManager = GridLayoutManager(requireContext(), 2).apply {
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
//        //                    liveEvent.setVideoViewForLiveEvent(binding.svvHost,liveEvent.hosts.first().hostId)
//
//        liveEvent.enter { e ->
//            if (e != null) {
//                Log.d("TESSSSTTT",e.message.toString())
//                return@enter
//
//            }else {
//                Log.d("Doneeeee","1234")
//
////                    liveEvent.setVideoViewForLiveEvent(binding.svvHost,liveEvent.hosts.first().hostId)
//
//            }
//        }
//        liveEvent.addListener("${UUID.randomUUID()}", object:LiveEventListener{
//            override fun onCustomItemsDelete(
//                liveEvent: LiveEvent,
//                customItems: Map<String, String>,
//                deletedKeys: List<String>
//            ) {
//
//            }
//
//            override fun onCustomItemsUpdate(
//                liveEvent: LiveEvent,
//                customItems: Map<String, String>,
//                updatedKeys: List<String>
//            ) {
//            }
//
//            override fun onDisconnected(liveEvent: LiveEvent, e: SendbirdException) {
//            }
//
//            override fun onExited(liveEvent: LiveEvent, e: SendbirdException) {
//            }
//
//            override fun onHostConnected(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onHostDisconnected(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onHostEntered(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onHostExited(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onHostMuteAudio(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onHostStartVideo(liveEvent: LiveEvent, host: Host) {
//
//            }
//
//            override fun onHostStopVideo(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onHostUnmuteAudio(liveEvent: LiveEvent, host: Host) {
//            }
//
//            override fun onLiveEventEnded(liveEvent: LiveEvent) {
//            }
//
//            override fun onLiveEventInfoUpdated(liveEvent: LiveEvent) {
//            }
//
//            override fun onLiveEventReady(liveEvent: LiveEvent) {
//            }
//
//            override fun onLiveEventStarted(liveEvent: LiveEvent) {
//            }
//
//            override fun onParticipantCountChanged(
//                liveEvent: LiveEvent,
//                participantCountInfo: ParticipantCountInfo
//            ) {
//            }
//
//            override fun onReactionCountUpdated(liveEvent: LiveEvent, key: String, count: Int) {
//            }
//
//            override fun onReconnected(liveEvent: LiveEvent) {
//            }
//
//        })
//
////        this.liveEvent = liveEvent
////        adapter = HostAdapter(liveEvent)
//
//
////        liveEvent.addListener("${UUID.randomUUID()}", liveEventListenerImpl)
//
//
//    }
//
//
////      fun attachToLiveEvent(liveEvent:LiveEvent) {
////        liveEvent ?: run {
////            showToast("LiveEvent unavailable")
////            return
////        }
////    }
////    protected fun initHostView() {
////
//////        val host = liveEvent?.host
//////        if (host != null) {
//////            adapter.addItems(listOf(host))
//////        }
////    }
//
//}
