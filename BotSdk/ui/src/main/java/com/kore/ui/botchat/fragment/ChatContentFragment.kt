package com.kore.ui.botchat.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.internal.LinkedTreeMap
import com.kore.SDKConfig.isMinimized
import com.kore.common.SDKConfiguration
import com.kore.common.event.UserActionEvent
import com.kore.common.utils.NetworkUtils
import com.kore.event.BotChatEvent
import com.kore.extensions.verticalSmoothScrollTo
import com.kore.model.BaseBotMessage
import com.kore.model.BotRequest
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.adapters.BotChatAdapter
import com.kore.ui.adapters.QuickRepliesTemplateAdapter
import com.kore.ui.base.BaseContentFragment
import com.kore.ui.databinding.BotContentLayoutBinding
import com.kore.ui.row.botchat.BotChatItemDecoration
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.TimeStampTemplateRow
import java.util.LinkedList
import java.util.Queue

class ChatContentFragment : BaseContentFragment() {
    private lateinit var binding: BotContentLayoutBinding
    private lateinit var chatAdapter: BotChatAdapter
    private lateinit var actionEvent: (event: BotChatEvent) -> Unit
    private val wordQueue: Queue<String?> = LinkedList<String?>()
    private var isStreamingRunning = false
    private var appendedWordCount = 0
    private val streamingHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatAdapter = BotChatAdapter(requireActivity(), BotChatRowType.getAllRowTypes())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BotContentLayoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.chatContentList.setItemViewCacheSize(100)
        binding.chatContentList.adapter = chatAdapter
        binding.chatContentList.addItemDecoration(BotChatItemDecoration(requireContext()))
        binding.swipeContainerChat.setOnRefreshListener {
            if (!SDKConfiguration.OverrideKoreConfig.paginatedScrollEnable) return@setOnRefreshListener
            if (!NetworkUtils.isNetworkAvailable(requireContext())) {
                Toast.makeText(context, getString(R.string.no_network), Toast.LENGTH_SHORT).show()
                binding.swipeContainerChat.isRefreshing = false
                return@setOnRefreshListener
            }
            //            this.view?.onSwipeRefresh()
            contentViewModel.fetchChatHistory(false)
        }
    }

    override fun onChatHistory(list: List<BaseBotMessage>, isReconnection: Boolean) {
        addMessagesToAdapter(list, !isMinimized(), isReconnection)
    }

    override fun onFileDownloadProgress(msgId: String, progress: Int, downloadBytes: Int) {
        chatAdapter.onDownloadProgress(msgId, progress, downloadBytes)
    }

    override fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit) {
        this.actionEvent = actionEvent
        chatAdapter.setActionEvent(actionEvent)
    }

    override fun getAdapterLastItem(): BaseBotMessage? {
        return chatAdapter.getAdapterLastItem()
    }

    override fun showTypingIndicator(icon: String?, enable: Boolean) {
        binding.botTypingStatus.isVisible = enable
        if (!icon.isNullOrEmpty() && isResumed) {
            Glide.with(requireActivity()).load(icon)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .error(R.drawable.ic_image_photo)
                .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.typingStatusItemCpv))
        }
    }

    override fun stopTypingIndicator() {
        binding.botTypingStatus.isVisible = false
    }

    override fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?, isStacked: Boolean) {
        if (quickReplies != null) {
            binding.quickReplyView.isVisible = true
            if (isStacked) {
                val layoutManager = FlexboxLayoutManager(binding.root.context)
                layoutManager.justifyContent = JustifyContent.FLEX_START
                layoutManager.flexDirection = FlexDirection.ROW
                binding.quickReplyView.layoutManager = layoutManager
            } else {
                binding.quickReplyView.layoutManager = LinearLayoutManager(
                    requireActivity(),
                    if (type.equals(BotResponseConstants.TEMPLATE_TYPE_LIST, ignoreCase = true))
                        LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
            binding.quickReplyView.adapter = QuickRepliesTemplateAdapter(requireActivity(), type, quickReplies, this)
        }
    }

    override fun hideQuickReplies() {
        binding.quickReplyView.isVisible = false
    }

    override fun addMessagesToAdapter(messages: List<BaseBotMessage>, isHistory: Boolean, isReconnection: Boolean) {
        binding.swipeContainerChat.isRefreshing = false
        if (messages.isEmpty()) return
        val rows = chatAdapter.addAndCreateRows(messages, isHistory, isReconnection)
        chatAdapter.submitList(rows)
        binding.chatContentList.postDelayed({
            val history = rows.filter { it !is TimeStampTemplateRow }
            contentViewModel.setHistoryOffset(history.size)
            if (!isHistory && chatAdapter.itemCount > 0) {
                binding.chatContentList.verticalSmoothScrollTo(chatAdapter.itemCount - 1, LinearSmoothScroller.SNAP_TO_START)
            }
            showTypingIndicator("", false)
        }, 100)
    }

    override fun onResendMessage(botRequest: BotRequest) {
        chatAdapter.onResendMessage(botRequest)
    }

    override fun getMessageById(msgId: String): BaseBotMessage {
        return chatAdapter.getMessageById(msgId)
    }

    override fun onLoadingHistory() {
        binding.swipeContainerChat.isRefreshing = true
    }

    override fun onLoadHistory(isReconnect: Boolean) {
        contentViewModel.loadChatHistory(isReconnect)
    }

    override fun addStreamingMessage(message: String?) {
        if (message?.isNotEmpty() == true) {
            chatAdapter.addStreamingMessage(message);
            binding.chatContentList.post(Runnable { binding.chatContentList.scrollBy(0, 1500) })
        }
    }

    override fun deleteMessage(messageId: String) {
        chatAdapter.deleteMessage(messageId)
    }

    //    override fun addStreamingMessage(message: String?) {
    //        if (message == null || message.isBlank()) return
    //
    //        wordQueue.add(message)
    //
    //        if (!isStreamingRunning) {
    //            processNextBatch()
    //        }
    //    }

    private fun processNextBatch() {
        if (wordQueue.isEmpty()) {
            isStreamingRunning = false
            return
        }

        isStreamingRunning = true

        val batchSize = getBatchSize()

        val builder = StringBuilder()

        for (i in 0..<batchSize) {
            val word: String = wordQueue.poll() ?: break

            builder.append(word).append(" ")
            appendedWordCount++
        }

        chatAdapter.addStreamingMessage(builder.toString())

        if (appendedWordCount % 3 == 0) {
            binding.chatContentList.post(Runnable { binding.chatContentList.scrollBy(0, 1500) })
        }

        streamingHandler.postDelayed(
            Runnable { this.processNextBatch() },
            getAdaptiveDelay().toLong()
        )
    }

    private fun getBatchSize(): Int {
        val size: Int = wordQueue.size
        if (size > 80) return 8
        if (size > 40) return 5
        if (size > 15) return 3

        return 1
    }

    private fun getAdaptiveDelay(): Int {
        val size: Int = wordQueue.size
        if (size > 80) return 25
        if (size > 40) return 30
        if (size > 15) return 40

        return 35
    }

    override fun getAdapterCount(): Int = chatAdapter.itemCount

    override fun quickRepliesClicked(quickReplyTemplate: Map<String, *>) {
        when (quickReplyTemplate[BotResponseConstants.CONTENT_UNDERSCORE_TYPE]) {
            BotResponseConstants.USER_INTENT, BotResponseConstants.URL, BotResponseConstants.WEB_URL -> {
                if (quickReplyTemplate[BotResponseConstants.URL] != null) {
                    actionEvent(BotChatEvent.UrlClick(quickReplyTemplate[BotResponseConstants.URL] as String))
                } else if (quickReplyTemplate[BotResponseConstants.WEB_URL] != null) {
                    actionEvent(BotChatEvent.UrlClick(quickReplyTemplate[BotResponseConstants.WEB_URL] as String))
                }
            }

            else -> {
                if (quickReplyTemplate[BotResponseConstants.PAYLOAD] != null) {
                    val payLoad = quickReplyTemplate[BotResponseConstants.PAYLOAD]
                    when (payLoad is LinkedTreeMap<*, *>) {
                        true -> {
                            val innerPayLoad =
                                (quickReplyTemplate[BotResponseConstants.PAYLOAD]) as Map<*, *>
                            if (innerPayLoad[BotResponseConstants.NAME] != null) {
                                actionEvent(BotChatEvent.SendMessage(innerPayLoad[BotResponseConstants.NAME] as String))
                            }
                        }

                        else -> {
                            actionEvent(
                                BotChatEvent.SendMessage(
                                    quickReplyTemplate[BotResponseConstants.KEY_TITLE] as String,
                                    quickReplyTemplate[BotResponseConstants.PAYLOAD] as String
                                )
                            )
                        }
                    }
                } else actionEvent(BotChatEvent.SendMessage(quickReplyTemplate[BotResponseConstants.KEY_TITLE] as String))
            }
        }
        binding.quickReplyView.isVisible = false
    }

    override fun onBrandingDetails() {

        chatAdapter.onBrandingDetails()
        binding.swipeContainerChat.isEnabled = SDKConfiguration.OverrideKoreConfig.paginatedScrollEnable
    }
}