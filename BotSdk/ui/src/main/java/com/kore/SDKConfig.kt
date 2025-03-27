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
    private var isUpdateStatusBarColor = false

    @JvmStatic
    fun initialize(botConfigModel: BotConfigModel) {
        SDKConfiguration.initialize(botConfigModel)
    }

    @JvmStatic
    fun setWidgetConfig(widgetConfig: WidgetConfigModel) {
        WidgetSDKConfiguration.initialize(widgetConfig)
    }

    @JvmStatic
    fun setLoginToken(token: String) {
        SDKConfiguration.setLoginToken(token)
    }

    @JvmStatic
    fun setQueryParams(queryParams: HashMap<String, Any>) {
        SDKConfiguration.setQueryParams(queryParams)
    }

    @JvmStatic
    fun setCustomData(customData: HashMap<String, Any>) {
        SDKConfiguration.setCustomData(customData)
    }

    @JvmStatic
    fun addCustomTemplate(providerName: String, templateType: String, provider: SimpleListViewHolderProvider<*>, templateRow: KClass<*>) {
        val rowType = BotChatRowType.createRowType(providerName, provider)
        customTemplates[templateType] = Pair(rowType, templateRow)
    }

    @JvmStatic
    fun getCustomTemplates(): HashMap<String, Pair<Any, KClass<*>>> = customTemplates

    @JvmStatic
    fun addCustomHeaderFragment(headerSize: String, fragment: BaseHeaderFragment) {
        customHeaders[headerSize] = fragment
    }

    @JvmStatic
    fun getCustomHeaderFragment(size: String): BaseHeaderFragment? = customHeaders[size]

    @JvmStatic
    fun addCustomContentFragment(contentFragment: BaseContentFragment) {
        customContentFragment = contentFragment
    }

    @JvmStatic
    fun getCustomContentFragment(): BaseContentFragment? = customContentFragment

    @JvmStatic
    fun addCustomFooterFragment(fragment: BaseFooterFragment) {
        customFooterFragment = fragment
    }

    @JvmStatic
    fun getCustomFooterFragment(): BaseFooterFragment? = customFooterFragment

    @JvmStatic
    fun setIsMinimized(isMinimized: Boolean) {
        this.isMinimized = isMinimized
    }

    @JvmStatic
    fun isMinimized(): Boolean = isMinimized

    @JvmStatic
    fun setIsShowIconTop(isShow: Boolean) {
        SDKConfiguration.OverrideKoreConfig.showIconTop = isShow
    }

    @JvmStatic
    fun setIsShowHamburgerMenu(isShow: Boolean) {
        SDKConfiguration.OverrideKoreConfig.showHamburgerMenu = isShow
    }

    @JvmStatic
    fun setBotBrandingConfig(botBrandingModel: BotBrandingModel?) {
        SDKConfiguration.setBotBrandingConfig(botBrandingModel)
    }

    @JvmStatic
    fun getBotBrandingConfig(): BotBrandingModel? = SDKConfiguration.getBotBrandingConfig()

    @JvmStatic
    fun setConnectionMode(connectionMode: String) {
        SDKConfiguration.setConnectionMode(connectionMode)
    }

    @JvmStatic
    fun setHistoryOnNetworkResume(loadHistory: Boolean) {
        historyOnNetworkResume = loadHistory
    }

    @JvmStatic
    fun getHistoryOnNetworkResume() = historyOnNetworkResume

    @JvmStatic
    fun setIsUpdateStatusBarColor(isUpdate: Boolean) {
        isUpdateStatusBarColor = isUpdate
    }

    @JvmStatic
    fun isUpdateStatusBarColor(): Boolean {
        return isUpdateStatusBarColor
    }
}