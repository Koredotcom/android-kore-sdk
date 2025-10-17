package com.kore.ui.botchat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import com.kore.common.SDKConfiguration
import com.kore.common.event.UserActionEvent
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.network.api.responsemodels.branding.BrandingHeaderModel
import com.kore.ui.base.BaseHeaderFragment
import com.kore.ui.databinding.BotV2HeaderBinding

class ChatV2HeaderFragment : BaseHeaderFragment() {
    private lateinit var binding: BotV2HeaderBinding
    private var brandingModel: BotBrandingModel? = null

    override fun setBrandingHeader(brandingModel: BrandingHeaderModel?) {
    }

    override fun setBrandingDetails(brandingModel: BotBrandingModel?) {
        this.brandingModel = brandingModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BotV2HeaderBinding.inflate(layoutInflater)
        updateUI()
        return binding.root
    }

    private fun updateUI() {
        binding.tvBotName.text = brandingModel?.header?.title?.name ?: SDKConfiguration.getBotConfigModel()?.botName
        brandingModel?.let {
            if (!brandingModel?.header?.bgColor.isNullOrEmpty()) it.general.colors.secondary?.toColorInt()
                ?.let { it1 -> binding.root.setBackgroundColor(it1) }
            if (!brandingModel?.header?.title?.color.isNullOrEmpty()) it.general.colors.primaryText?.toColorInt()
                ?.let { it1 -> binding.tvBotName.setTextColor(it1) }
        }
    }

    override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
    }
}