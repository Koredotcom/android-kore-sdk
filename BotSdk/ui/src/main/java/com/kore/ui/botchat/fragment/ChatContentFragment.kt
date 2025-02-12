package com.kore.ui.botchat.fragment

import android.os.Bundle
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
import com.google.gson.internal.LinkedTreeMap
import com.kore.common.SDKConfiguration
import com.kore.ui.base.BaseView
import com.kore.common.event.UserActionEvent
import com.kore.common.utils.NetworkUtils
import com.kore.event.BotChatEvent
import com.kore.extensions.verticalSmoothScrollTo
import com.kore.model.BaseBotMessage
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.adapters.BotChatAdapter
import com.kore.ui.adapters.QuickRepliesTemplateAdapter
import com.kore.ui.base.BaseContentFragment
import com.kore.ui.botchat.BotChatView
import com.kore.ui.databinding.BotContentLayoutBinding
import com.kore.ui.row.botchat.BotChatItemDecoration
import com.kore.ui.row.botchat.BotChatRowType

class ChatContentFragment : BaseContentFragment() {
    private lateinit var binding: BotContentLayoutBinding
    private lateinit var chatAdapter: BotChatAdapter
    private var view: BotChatView? = null
    private lateinit var actionEvent: (event: BotChatEvent) -> Unit

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
            this.view?.onSwipeRefresh()
        }
    }

    override fun setView(view: BaseView) {
        this.view = view as BotChatView
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

    override fun showQuickReplies(quickReplies: List<Map<String, *>>?, type: String?) {
        if (quickReplies != null) {
            binding.quickReplyView.isVisible = true
            binding.quickReplyView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.quickReplyView.adapter = QuickRepliesTemplateAdapter(requireActivity(), type, quickReplies, this)
        }
    }

    override fun hideQuickReplies() {
        binding.quickReplyView.isVisible = false
    }

    override fun addMessagesToAdapter(messages: List<BaseBotMessage>, isHistory: Boolean) {
        binding.swipeContainerChat.isRefreshing = false
        if (messages.isEmpty()) return
        chatAdapter.submitList(chatAdapter.addAndCreateRows(messages, isHistory))
        binding.chatContentList.postDelayed({
            if (!isHistory && chatAdapter.itemCount > 0) {
                binding.chatContentList.verticalSmoothScrollTo(chatAdapter.itemCount - 1, LinearSmoothScroller.SNAP_TO_START)
            }
            showTypingIndicator("", false)
        }, 100)
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