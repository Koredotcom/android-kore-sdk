package com.kore.ui.botchat.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            if (!brandingModel?.general?.colors?.secondary.isNullOrEmpty()) binding.root.setBackgroundColor(Color.parseColor(it.general.colors.secondary))
            if (!brandingModel?.general?.colors?.secondary.isNullOrEmpty()) binding.tvBotName.setTextColor(Color.parseColor(it.general.colors.primaryText))
        }
    }

    override fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit) {
    }
}