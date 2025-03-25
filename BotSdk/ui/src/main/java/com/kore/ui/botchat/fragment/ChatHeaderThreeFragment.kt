package com.kore.ui.botchat.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.kore.common.SDKConfiguration
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingHeaderModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.ui.R
import com.kore.ui.base.BaseHeaderFragment
import com.kore.ui.databinding.BotHeader3Binding

class ChatHeaderThreeFragment : BaseHeaderFragment() {
    private lateinit var binding: BotHeader3Binding
    private var onActionEvent: (event: UserActionEvent) -> Unit = {}
    private var brandingModel: BrandingHeaderModel? = null

    override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
        this.onActionEvent = onActionEvent
    }

    override fun setBrandingHeader(brandingModel: BrandingHeaderModel?) {
        this.brandingModel = brandingModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BotHeader3Binding.inflate(layoutInflater)

        brandingModel?.let { model ->
            val title = model.title?.name
            binding.tvBotTitle.text = if (!title.isNullOrEmpty()) title else SDKConfiguration.getBotConfigModel()?.botName
            binding.tvBotDesc.text = model.subTitle?.name
            model.bgColor?.toColorInt()?.let { binding.root.setBackgroundColor(it) }

            if (model.icon != null) {
                if (model.icon?.type == (BotResponseConstants.CUSTOM)) {
                    binding.llBotAvatar.setBackgroundResource(0)
                    Glide.with(requireActivity())
                        .load(model.icon?.iconUrl)
                        .into(binding.ivBotAvatar)
                        .onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_foreground, context?.theme))
                    binding.ivBotAvatar.layoutParams =
                        LinearLayout.LayoutParams(40.dpToPx(binding.root.context), 40.dpToPx(binding.root.context))
                } else {
                    when (model.icon?.iconUrl) {
                        BotResponseConstants.ICON_1 -> binding.ivBotAvatar.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_1, context?.theme)
                        )

                        BotResponseConstants.ICON_2 -> binding.ivBotAvatar.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_2, context?.theme)
                        )

                        BotResponseConstants.ICON_3 -> binding.ivBotAvatar.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_3, context?.theme)
                        )

                        BotResponseConstants.ICON_4 -> binding.ivBotAvatar.setImageDrawable(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_4, context?.theme)
                        )
                    }
                }
            }

            model.buttons?.let { buttons ->
                val bgColorTint = model.iconsColor?.toColorInt()?.let { ColorStateList.valueOf(it) }
                binding.ivBotArrowBack.backgroundTintList = bgColorTint
                binding.ivBotHelp.isVisible = buttons.help?.show == true
                binding.ivBotSupport.isVisible = buttons.liveAgent?.show == true
                binding.ivBotClose.isVisible = buttons.close?.show == true
                binding.ivBotHelp.backgroundTintList = bgColorTint
                binding.ivBotSupport.backgroundTintList = bgColorTint
                binding.ivBotClose.backgroundTintList = bgColorTint
                buttons.help?.let { help -> binding.ivBotHelp.setOnClickListener { onClick(help.action) } }
                buttons.liveAgent?.let { liveAgent -> binding.ivBotSupport.setOnClickListener { onClick(liveAgent.action) } }
                buttons.close?.let { close -> binding.ivBotClose.setOnClickListener { onClick(close.action) } }
            }
        }

        binding.ivBotArrowBack.setOnClickListener {
            onActionEvent(BotChatEvent.OnBackPressed)
        }

        binding.ivBotClose.setOnClickListener {
            onActionEvent(BotChatEvent.OnBackPressed)
        }

        return binding.root
    }

    private fun onClick(actionModel: BrandingQuickStartButtonActionModel?) {
        actionModel?.let { action ->
            when (action.type) {
                BotResponseConstants.BUTTON_TYPE_URL, BotResponseConstants.BUTTON_TYPE_WEB_URL -> {
                    action.value?.let { it1 -> BotChatEvent.UrlClick(it1) }?.let { it2 -> onActionEvent(it2) }
                }

                else -> (action.value ?: action.title)?.let { onActionEvent(BotChatEvent.SendMessage(it)) }
            }
        }
    }
}