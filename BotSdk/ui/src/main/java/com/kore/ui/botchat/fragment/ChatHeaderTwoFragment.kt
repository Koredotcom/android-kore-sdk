package com.kore.ui.botchat.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.kore.common.event.UserActionEvent
import com.kore.common.extensions.getParcelableCompat
import com.kore.common.utils.DimensionUtils
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingHeaderModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.ui.R
import com.kore.ui.botchat.BotChatActivity.Companion.EXTRA_BOT_HEADER
import com.kore.ui.databinding.BotHeader2Binding

class ChatHeaderTwoFragment(private val onActionEvent: (event: UserActionEvent) -> Unit) : Fragment() {

    lateinit var binding: BotHeader2Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = BotHeader2Binding.inflate(layoutInflater)

        val bundle = this.arguments?.getParcelableCompat(EXTRA_BOT_HEADER) as BrandingHeaderModel?

        if (bundle != null) {
            binding.tvBotTitle.text = bundle.title?.name
            binding.tvBotDesc.text = bundle.subTitle?.name
            binding.root.setBackgroundColor(Color.parseColor(bundle.bgColor))

            if (bundle.icon != null) {
                if (bundle.icon?.type == (BotResponseConstants.CUSTOM)) {
                    binding.llBotAvatar.setBackgroundResource(0)
                    Glide.with(requireActivity()).load(bundle.icon?.iconUrl).into(binding.ivBotAvatar)
                        .onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_foreground, context?.theme))
                    binding.ivBotAvatar.layoutParams =
                        LinearLayout.LayoutParams((40 * DimensionUtils.dp1).toInt(), (40 * DimensionUtils.dp1).toInt())
                } else {
                    when (bundle.icon?.iconUrl) {
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

            bundle.buttons?.let { buttons ->
                val bgColorTint = ColorStateList.valueOf(Color.parseColor(bundle.iconsColor))
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