package com.kore.widgets.ui.fragments.bottompanel.row.widget

import android.app.Activity
import android.content.Context
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.widgets.R
import com.kore.widgets.constants.Constants.HEADER_TYPE_BUTTON
import com.kore.widgets.constants.Constants.HEADER_TYPE_IMAGE
import com.kore.widgets.constants.Constants.HEADER_TYPE_MENU
import com.kore.widgets.constants.Constants.HEADER_TYPE_TEXT
import com.kore.widgets.constants.Constants.HEADER_TYPE_URL
import com.kore.widgets.constants.Constants.WIDGET_TYPE_FORM
import com.kore.widgets.constants.Constants.WIDGET_TYPE_LIST
import com.kore.widgets.constants.Constants.WIDGET_TYPE_LOGIN_URL
import com.kore.widgets.constants.Constants.WIDGET_TYPE_PIE_CHART
import com.kore.widgets.databinding.RowPanelWidgetBinding
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.event.WidgetActionEvent
import com.kore.widgets.model.ButtonModel
import com.kore.widgets.model.WidgetInfoModel
import com.kore.widgets.model.WidgetsModel
import com.kore.widgets.row.WidgetSimpleListAdapter
import com.kore.widgets.row.WidgetSimpleListRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.WidgetInfoRowType
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.form.FormWidgetRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.list.ListWidgetRow
import com.kore.widgets.ui.fragments.bottompanel.row.widget.row.piechart.PieChartWidgetRow
import com.kore.widgets.utils.WidgetUtils.launchDialer
import com.kore.widgets.utils.WidgetUtils.showEmailIntent
import com.squareup.picasso.Picasso

class PanelWidgetRow(
    private val widgetsModel: WidgetsModel,
    private val widgetInfoModel: WidgetInfoModel?,
    private val showMenus: (menus: List<ButtonModel>) -> Unit,
    private val reloadWidget: (widgetModel: WidgetsModel) -> Unit,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : WidgetSimpleListRow {
    override val type: WidgetSimpleListRow.SimpleListRowType = PanelWidgetRowType.Widget

    override fun areItemsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is PanelWidgetRow) return false
        return otherRow.widgetsModel.id == widgetsModel.id
    }

    override fun areContentsTheSame(otherRow: WidgetSimpleListRow): Boolean {
        if (otherRow !is PanelWidgetRow) return false
        return otherRow.widgetsModel == widgetsModel && otherRow.widgetInfoModel == widgetInfoModel
    }

    override fun getChangePayload(otherRow: WidgetSimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as RowPanelWidgetBinding).apply {
            widgetLayout.layoutManager = LinearLayoutManager(root.context)
            widgetLayout.adapter = WidgetSimpleListAdapter(WidgetInfoRowType.entries)
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        (binding as RowPanelWidgetBinding).commonBind()
    }

    private fun RowPanelWidgetBinding.commonBind() {
        meetingHeader.text = widgetsModel.title
        if (widgetsModel.callbackURL.isNullOrEmpty()) return
        meetingProgress.isVisible = widgetInfoModel == null
        widgetInfoModel?.let { infoModel ->
            val headerOptions = infoModel.headerOptions
            if (headerOptions?.type != null) {
                iconImageLoad.isVisible = headerOptions.type == HEADER_TYPE_IMAGE
                iconImage.isVisible = headerOptions.type == HEADER_TYPE_MENU
                tvText.isVisible = headerOptions.type == HEADER_TYPE_TEXT
                tvUrl.isVisible = headerOptions.type == HEADER_TYPE_URL
                tvValuesLayout.isVisible = headerOptions.type == HEADER_TYPE_BUTTON

                when (headerOptions.type) {
                    HEADER_TYPE_BUTTON -> {
                        val btnTitle = if (headerOptions.button != null && headerOptions.button?.title != null) {
                            headerOptions.button?.title
                        } else {
                            headerOptions.text
                        }
                        if (!btnTitle.isNullOrEmpty()) tvButton.text = btnTitle else tvValuesLayout.isVisible = false
                        tvButton.setOnClickListener { buttonAction(root.context, headerOptions.button) }
                    }

                    HEADER_TYPE_TEXT -> tvText.text = headerOptions.text

                    HEADER_TYPE_URL -> {
                        val content = SpannableString(headerOptions.url?.title ?: headerOptions.url?.link)
                        content.setSpan(UnderlineSpan(), 0, content.length, 0)
                        tvUrl.text = content
                        tvUrl.setOnClickListener {
                            headerOptions.url?.link?.let {
                                actionEvent(WidgetActionEvent.UrlClick(it, root.context.resources.getString(R.string.app_name)))
                            }
                        }
                    }

                    HEADER_TYPE_IMAGE -> {
                        headerOptions.image?.imageSrc?.let {
                            Picasso.get().load(it).into(iconImageLoad)
                            iconImageLoad.setOnClickListener {
                                buttonAction(
                                    root.context,
                                    headerOptions.image?.utterance ?: headerOptions.image?.payload ?: ""
                                )
                            }
                        }
                    }

                    HEADER_TYPE_MENU -> {
                        iconImage.setOnClickListener {
                            if (!headerOptions.menu.isNullOrEmpty()) {
                                showMenus(headerOptions.menu!!)
                            }
                        }
                    }
                }
            }
            needLogin.root.isVisible = infoModel.templateType == "loginURL"
            error.root.isVisible = infoModel.templateType == null
            when (infoModel.templateType) {
                WIDGET_TYPE_LOGIN_URL -> {
                    needLogin.loginButton.setOnClickListener {
                        if (!infoModel.loginModel?.url.isNullOrEmpty()) {
                            actionEvent(WidgetActionEvent.UrlClick(infoModel.loginModel?.url!!, widgetsModel.title!!))
                        }
                    }
                }

                WIDGET_TYPE_FORM -> {
                    (widgetLayout.adapter as WidgetSimpleListAdapter).apply {
                        submitList(listOf(FormWidgetRow(widgetsModel, infoModel, actionEvent)))
                    }
                }

                WIDGET_TYPE_LIST -> {
                    (widgetLayout.adapter as WidgetSimpleListAdapter).apply {
                        submitList(listOf(ListWidgetRow(widgetsModel, infoModel, actionEvent)))
                    }
                }

                WIDGET_TYPE_PIE_CHART -> {
                    (widgetLayout.adapter as WidgetSimpleListAdapter).apply {
                        submitList(listOf(PieChartWidgetRow("", widgetsModel, widgetInfoModel, actionEvent)))
                    }
                }

                null -> {
                    error.refresh.setOnClickListener { reloadWidget(widgetsModel) }
                }

                else -> {
                    Log.i("PanelWidgetRow", "Widget layout not available.")
                }
            }
        }
    }

    private fun buttonAction(context: Context, utt: String?) {
        if (utt == null) return
        if (utt.startsWith("tel:") || utt.startsWith("mailto:")) {
            if (utt.startsWith("tel:")) {
                launchDialer(context, utt)
            } else if (utt.startsWith("mailto:")) {
                showEmailIntent(
                    context as Activity,
                    utt.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                )
            }
            return
        }
        actionEvent(WidgetActionEvent.SendMessageFromPanel(utt, true))
    }

    private fun buttonAction(context: Context, button: ButtonModel?) {
        var utterance: String? = null
        if (button != null) utterance = button.payload
        if (utterance == null) return
        if (utterance.startsWith("tel:") || utterance.startsWith("mailto:")) {
            if (utterance.startsWith("tel:")) {
                launchDialer(context, utterance)
            } else if (utterance.startsWith("mailto:")) {
                showEmailIntent(
                    context as Activity,
                    utterance.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                )
            }
            return
        }
        actionEvent(WidgetActionEvent.SendMessageFromPanel(utterance, true, utterance))
    }
}