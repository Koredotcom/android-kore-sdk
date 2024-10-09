package com.kore.widgets.ui.fragments.bottompanel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.kore.widgets.Result
import com.kore.widgets.WidgetSDKConfiguration
import com.kore.widgets.data.repository.jwt.WidgetJwtRepository
import com.kore.widgets.data.repository.jwt.WidgetJwtRepositoryImpl
import com.kore.widgets.data.repository.panel.WidgetPanelRepository
import com.kore.widgets.data.repository.panel.WidgetPanelRepositoryImpl
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.model.Panel
import com.kore.widgets.model.WidgetInfoModel
import com.kore.widgets.model.WidgetsModel
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.bottompanel.row.BottomPanelRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.PanelWidgetRow
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class BottomPanelFragmentViewModel : ViewModel() {
    private val jwtRepository: WidgetJwtRepository = WidgetJwtRepositoryImpl()
    private val widgetPanelRepository: WidgetPanelRepository = WidgetPanelRepositoryImpl()
    private lateinit var view: BottomPanelView
    private var userJwtToken = ""
    private var jwtToken = ""
    private var widgetsInfo = mutableMapOf<String, WidgetInfoModel>()
    private var actionEvent: (event: BaseActionEvent) -> Unit = {}
    private var selectedPanel: Panel? = null
    private var isPanelOpen = false
    private val handler = Handler(Looper.getMainLooper())

    private val headers by lazy {
        HashMap<String, Any>().apply {
            put("alg", "RS256")
            put("typ", "JWT")
            put("Content-Type", "application/json; charset=UTF-8")
        }
    }

    private val body by lazy {
        HashMap<String, Any>().apply {
            put("isAnonymous", false)
            put("clientId", WidgetSDKConfiguration.getWidgetConfigModel()?.clientId!!)
            put("identity", WidgetSDKConfiguration.getWidgetConfigModel()?.identity!!)
            put("aud", "https://idproxy.kore.com/authorize")
            put("clientSecret", WidgetSDKConfiguration.getWidgetConfigModel()?.clientSecret!!)
        }
    }

    companion object {
        private const val ONE_MINUTE = 1000 * 60
    }

    fun setView(view: BottomPanelView) {
        this.view = view
    }

    fun init(userJwtToken: String) {
        this.userJwtToken = userJwtToken
        view.init()
        getPanelList()
    }

    fun setIsPanelOpen(isPanelOpen: Boolean) {
        if (this.isPanelOpen && !isPanelOpen) {
            handler.removeCallbacksAndMessages(null)
            selectedPanel = null
            widgetsInfo.clear()
        }
        this.isPanelOpen = isPanelOpen
    }

    fun setUserActionEvent(actionEvent: (event: BaseActionEvent) -> Unit) {
        this.actionEvent = actionEvent
    }

    private fun getPanelList() {
        val configModel = WidgetSDKConfiguration.getWidgetConfigModel() ?: return
        MainScope().launch {
            jwtToken = userJwtToken.ifEmpty { jwtRepository.getJwtToken(configModel, headers, body) }
            when (val result = widgetPanelRepository.getWidgetPanelData(configModel.botId, jwtToken, configModel.identity)) {
                is Result.Success -> {
                    result.data?.let {
                        if (selectedPanel != null) {
                            it.map { item -> if (item.id == selectedPanel?.id) selectedPanel = item }
                        }
                        view.onBottomPanels(createPanelRows(it))
                    }
                }

                else -> view.onBottomPanelError()
            }
        }
    }

    private fun getAllWidgetInfo(panel: Panel) {
        MainScope().launch { panel.widgets.map { widget -> getWidgetInfo(widget, panel) } }
    }

    private fun reloadWidget(widgetsModel: WidgetsModel) {
        if (!isPanelOpen) return
        selectedPanel?.let {
            widgetsInfo.remove(widgetsModel.id)
            view.onPanelWidgets(createWidgetRows(it.widgets))
            MainScope().launch { getWidgetInfo(widgetsModel, it) }
        }
    }

    private suspend fun getWidgetInfo(widgetsModel: WidgetsModel, panel: Panel) {
        if (widgetsModel.callbackURL == null) return
        val map = HashMap<String, Any>().apply {
            put("input", emptyMap<String, Any>())
            put("from", WidgetSDKConfiguration.getWidgetConfigModel()?.identity!!)
        }
        when (val response = widgetPanelRepository.getWidgetInfo(widgetsModel.callbackURL, jwtToken, map)) {
            is Result.Success -> {
                widgetsInfo[widgetsModel.id] = response.data.data[0]
                view.onPanelWidgets(createWidgetRows(panel.widgets))
            }

            is Result.Error -> {
                widgetsInfo[widgetsModel.id] = WidgetInfoModel()
                view.onPanelWidgets(createWidgetRows(panel.widgets))
            }
        }
        if (isPanelOpen && widgetsModel.autoRefresh.enabled) {
            handler.postDelayed(
                { MainScope().launch { getWidgetInfo(widgetsModel, panel) } },
                (widgetsModel.autoRefresh.interval * ONE_MINUTE).toLong()
            )
        }
    }

    private fun createPanelRows(panels: List<Panel>): List<WidgetSimpleListRow> {
        return panels.map {
            BottomPanelRow(it) { selectedItem ->
                val list = panels.map { panel -> panel.copy(isItemClicked = selectedItem.id == panel.id, theme = panel.theme) }
                if (selectedPanel != null && selectedPanel?.id != selectedItem.id) widgetsInfo.clear()
                view.onBottomPanels(createPanelRows(list))
                view.onPanelItemClicked(selectedItem)
                view.onPanelWidgets(createWidgetRows(selectedItem.widgets))
                if (selectedPanel != null && selectedPanel?.id == selectedItem.id) return@BottomPanelRow
                selectedPanel = selectedItem
                handler.postDelayed({ getAllWidgetInfo(selectedItem) }, 300)
            }
        }
    }

    private fun createWidgetRows(widgets: List<WidgetsModel>): List<WidgetSimpleListRow> {
        return widgets.map { widgetModel ->
            PanelWidgetRow(widgetModel, widgetsInfo[widgetModel.id], view::showMenus, this::reloadWidget, actionEvent)
        }
    }
}