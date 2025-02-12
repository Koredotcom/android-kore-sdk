package com.kore.ui.base

import androidx.fragment.app.Fragment
import com.kore.common.event.UserActionEvent
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.network.api.responsemodels.branding.BrandingHeaderModel

abstract class BaseHeaderFragment : Fragment() {
    abstract fun setActionEvent(onActionEvent: (event: UserActionEvent) -> Unit)
    abstract fun setBrandingHeader(brandingModel: BrandingHeaderModel?)
    open fun setBrandingDetails(brandingModel: BotBrandingModel?){}
}