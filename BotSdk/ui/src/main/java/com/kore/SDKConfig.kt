package com.kore

import com.kore.common.SDKConfiguration
import com.kore.common.model.BotConfigModel
import com.kore.network.api.responsemodels.branding.BotBrandingModel
import com.kore.ui.base.BaseContentFragment
import com.kore.ui.base.BaseFooterFragment
import com.kore.ui.base.BaseHeaderFragment
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.widgets.WidgetSDKConfiguration
import com.kore.widgets.model.WidgetConfigModel
import kotlin.reflect.KClass

object SDKConfig {
    private val customHeaders: HashMap<String, BaseHeaderFragment> = HashMap()
    private val customTemplates: HashMap<String, Pair<Any, KClass<*>>> = HashMap()
    private var customContentFragment: BaseContentFragment? = null
    private var customFooterFragment: BaseFooterFragment? = null
    private var isMinimized: Boolean = false
    private var historyOnNetworkResume = true

    fun initialize(botConfigModel: BotConfigModel) {
        SDKConfiguration.initialize(botConfigModel)
    }

    fun setWidgetConfig(widgetConfig: WidgetConfigModel) {
        WidgetSDKConfiguration.initialize(widgetConfig)
    }

    fun setLoginToken(token: String) {
        SDKConfiguration.setLoginToken(token)
    }

    fun setQueryParams(queryParams: HashMap<String, Any>) {
        SDKConfiguration.setQueryParams(queryParams)
    }

    fun setCustomData(customData: HashMap<String, Any>) {
        SDKConfiguration.setCustomData(customData)
    }

    fun addCustomTemplate(providerName: String, templateType: String, provider: SimpleListViewHolderProvider<*>, templateRow: KClass<*>) {
        val rowType = BotChatRowType.createRowType(providerName, provider)
        customTemplates[templateType] = Pair(rowType, templateRow)
    }

    fun getCustomTemplates(): HashMap<String, Pair<Any, KClass<*>>> = customTemplates

    fun addCustomHeaderFragment(headerSize: String, fragment: BaseHeaderFragment) {
        customHeaders[headerSize] = fragment
    }

    fun getCustomHeaderFragment(size: String): BaseHeaderFragment? = customHeaders[size]

    fun addCustomContentFragment(contentFragment: BaseContentFragment) {
        customContentFragment = contentFragment
    }

    fun getCustomContentFragment(): BaseContentFragment? = customContentFragment

    fun addCustomFooterFragment(fragment: BaseFooterFragment) {
        customFooterFragment = fragment
    }

    fun getCustomFooterFragment(): BaseFooterFragment? = customFooterFragment

    fun setIsMinimized(isMinimized: Boolean) {
        this.isMinimized = isMinimized
    }

    fun isMinimized(): Boolean = isMinimized

    fun setIsShowIconTop(isShow: Boolean) {
        SDKConfiguration.OverrideKoreConfig.showIconTop = isShow
    }

    fun setIsShowHamburgerMenu(isShow: Boolean) {
        SDKConfiguration.OverrideKoreConfig.showHamburgerMenu = isShow
    }

    fun setBotBrandingConfig(botBrandingModel: BotBrandingModel?) {
        SDKConfiguration.setBotBrandingConfig(botBrandingModel)
    }

    fun getBotBrandingConfig(): BotBrandingModel? = SDKConfiguration.getBotBrandingConfig()

    fun setConnectionMode(connectionMode: String) {
        SDKConfiguration.setConnectionMode(connectionMode)
    }

    fun setHistoryOnNetworkResume(loadHistory: Boolean) {
        historyOnNetworkResume = loadHistory
    }

    fun getHistoryOnNetworkResume() = historyOnNetworkResume
}