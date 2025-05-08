package com.kore.ui.botchat.fragment

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kore.common.SDKConfiguration
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_BG_COLOR
import com.kore.model.constants.BotResponseConstants.BUBBLE_RIGHT_TEXT_COLOR
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.network.api.responsemodels.branding.BrandingHeaderModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.ui.R
import com.kore.ui.base.BaseHeaderFragment
import com.kore.ui.databinding.BotHeaderBinding

class ChatHeaderOneFragment : BaseHeaderFragment() {
    private lateinit var binding: BotHeaderBinding
    private var onActionEvent: (event: UserActionEvent) -> Unit = {}
    private var brandingModel: BrandingHeaderModel? = null
    private val preferenceRepository = PreferenceRepositoryImpl()

    override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
        this.onActionEvent = onActionEvent
    }

    override fun setBrandingHeader(brandingModel: BrandingHeaderModel?) {
        this.brandingModel = brandingModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BotHeaderBinding.inflate(layoutInflater)

        brandingModel?.let { model ->
            val title = model.title?.name
            binding.tvBotTitle.text = if (!title.isNullOrEmpty()) title else SDKConfiguration.getBotConfigModel()?.botName
            binding.tvBotDesc.text = model.subTitle?.name
            model.bgColor?.toColorInt()?.let { binding.root.setBackgroundColor(it) }

            if (model.icon != null) {
                if (model.icon?.type == (BotResponseConstants.CUSTOM)) {
                    binding.llBotAvatar.setBackgroundResource(0)
                    Glide.with(requireActivity()).load(model.icon?.iconUrl).apply(RequestOptions.bitmapTransform(CircleCrop()))
                        .into(binding.ivBotAvatar)
                        .onLoadFailed(ResourcesCompat.getDrawable(resources, R.drawable.ic_launcher_foreground, context?.theme))
                    binding.ivBotAvatar.layoutParams =
                        LinearLayout.LayoutParams((40.dpToPx(binding.root.context)), (40.dpToPx(binding.root.context)))
                } else {
                    val color = preferenceRepository.getStringValue(requireContext(), THEME_NAME, BUBBLE_RIGHT_BG_COLOR, "#ffffff")
                    binding.llBotAvatar.backgroundTintList = ColorStateList.valueOf(color.toColorInt())
                    val icon = when (model.icon?.iconUrl) {
                        BotResponseConstants.ICON_1 ->
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_1, context?.theme) as VectorDrawable

                        BotResponseConstants.ICON_2 ->
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_2, context?.theme) as VectorDrawable

                        BotResponseConstants.ICON_3 ->
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_3, context?.theme) as VectorDrawable

                        BotResponseConstants.ICON_4 ->
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_icon_4, context?.theme) as VectorDrawable

                        else -> ResourcesCompat.getDrawable(resources, R.mipmap.icon_avatar, context?.theme)
                    }

                    if (icon is VectorDrawable)
                        icon.setTint(preferenceRepository.getStringValue(requireContext(), THEME_NAME, BUBBLE_RIGHT_TEXT_COLOR, "#ffffff").toColorInt())
                    binding.ivBotAvatar.setImageDrawable(icon)
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