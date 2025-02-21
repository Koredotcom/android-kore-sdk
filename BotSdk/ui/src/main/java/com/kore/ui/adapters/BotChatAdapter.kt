package com.kore.ui.adapters

import android.content.Context
import com.kore.SDKConfig
import com.kore.common.event.UserActionEvent
import com.kore.common.utils.LogUtils
import com.kore.model.BaseBotMessage
import com.kore.model.BotRequest
import com.kore.model.BotResponse
import com.kore.model.PayloadOuter
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.COLOR
import com.kore.model.constants.BotResponseConstants.COMPONENT_TYPE_AUDIO
import com.kore.model.constants.BotResponseConstants.COMPONENT_TYPE_IMAGE
import com.kore.model.constants.BotResponseConstants.COMPONENT_TYPE_VIDEO
import com.kore.model.constants.BotResponseConstants.DIRECTION
import com.kore.model.constants.BotResponseConstants.DISPLAY_LIMIT
import com.kore.model.constants.BotResponseConstants.ELEMENTS
import com.kore.model.constants.BotResponseConstants.HEADING
import com.kore.model.constants.BotResponseConstants.KEY_BUTTONS
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.model.constants.BotResponseConstants.PIE_TYPE
import com.kore.model.constants.BotResponseConstants.PROGRESS
import com.kore.model.constants.BotResponseConstants.SELECTED_FEEDBACK
import com.kore.model.constants.BotResponseConstants.SELECTED_ITEM
import com.kore.model.constants.BotResponseConstants.SELECTED_POSITION
import com.kore.model.constants.BotResponseConstants.SELECTED_TIME
import com.kore.model.constants.BotResponseConstants.SLIDER_VIEW
import com.kore.model.constants.BotResponseConstants.TEXT_MESSAGE
import com.kore.ui.R
import com.kore.ui.row.SimpleListAdapter
import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.botchat.AdvanceTemplateRow
import com.kore.ui.row.botchat.BarChartTemplateRow
import com.kore.ui.row.botchat.BotChatRowType
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_REQUEST_MSG_PROVIDER
import com.kore.ui.row.botchat.BotChatRowType.Companion.ROW_RESPONSE_MSG_PROVIDER
import com.kore.ui.row.botchat.BotListTemplateRow
import com.kore.ui.row.botchat.ButtonTemplateRow
import com.kore.ui.row.botchat.CardTemplateRow
import com.kore.ui.row.botchat.CarouselStackedTemplateRow
import com.kore.ui.row.botchat.CarouselTemplateRow
import com.kore.ui.row.botchat.ClockTemplateRow
import com.kore.ui.row.botchat.DropDownTemplateRow
import com.kore.ui.row.botchat.FeedbackTemplateRow
import com.kore.ui.row.botchat.ImageTemplateRow
import com.kore.ui.row.botchat.LineChartTemplateRow
import com.kore.ui.row.botchat.ListWidgetTemplateRow
import com.kore.ui.row.botchat.MiniTableTemplateRow
import com.kore.ui.row.botchat.OtpTemplateRow
import com.kore.ui.row.botchat.PieChartTemplateRow
import com.kore.ui.row.botchat.ResetPinTemplateRow
import com.kore.ui.row.botchat.ResultsTemplateRow
import com.kore.ui.row.botchat.TableResponsiveRow
import com.kore.ui.row.botchat.TableTemplateRow
import com.kore.ui.row.botchat.TextTemplateRow
import com.kore.ui.row.botchat.TimeStampTemplateRow
import com.kore.ui.row.botchat.VideoTemplateRow
import com.kore.ui.row.botchat.advancemultiselect.AdvanceMultiSelectTemplateRow
import com.kore.ui.row.botchat.article.ArticleTemplateRow
import com.kore.ui.row.botchat.form.FormTemplateRow
import com.kore.ui.row.botchat.listview.ListViewTemplateRow
import com.kore.ui.row.botchat.multiselect.MultiSelectTemplateRow
import com.kore.ui.row.botchat.radiooptions.RadioOptionsTemplateRow
import com.kore.ui.row.botchat.tablelist.TableListTemplateRow
import com.kore.ui.row.listener.ChatContentStateListener

class BotChatAdapter(private val context: Context, types: List<SimpleListRow.SimpleListRowType>) : SimpleListAdapter(types),
    ChatContentStateListener {
    private var messages: List<BaseBotMessage> = emptyList()
    private var actionEvent: (event: UserActionEvent) -> Unit = {}

    private fun createTextTemplate(
        msgId: String, isRequest: Boolean, msg: String?, iconUrl: String?, isLastItem: Boolean, msgTime: String = "", errorTextColor: String = ""
    ): TextTemplateRow {
        val rowType = BotChatRowType.getRowType(if (isRequest) ROW_REQUEST_MSG_PROVIDER else ROW_RESPONSE_MSG_PROVIDER)
        return TextTemplateRow(rowType, context, msgId, msg, isRequest, iconUrl, isLastItem, actionEvent, msgTime, errorTextColor)
    }

    fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit) {
        this.actionEvent = actionEvent
    }

    fun addAndCreateRows(baseBotMessages: List<BaseBotMessage>, isHistory: Boolean = false, isReconnection: Boolean): List<SimpleListRow> {
        messages = if (isHistory) {
            if (!isReconnection) {
                messages + baseBotMessages
            } else {
                var latestChatMsg: BotResponse? = null
                for (botMessage in messages.reversed()) {
                    if (botMessage is BotResponse) {
                        latestChatMsg = botMessage
                        break
                    }
                }
                if (latestChatMsg != null) {
                    for (historyItem in baseBotMessages) {
                        if (historyItem.timeMillis > latestChatMsg.timeMillis) {
                            messages += historyItem
                        }
                    }
                    messages
                } else {
                    messages + baseBotMessages
                }
            }.distinctBy { it.messageId }.sortedBy { it.timeMillis }
        } else {
            messages + baseBotMessages
        }

        return createRows(messages)
    }

    fun onDownloadProgress(msgId: String, progress: Int, downloadBytes: Int) {
        onSaveState(msgId, if (progress == 0 && downloadBytes > 0) downloadBytes else progress, PROGRESS)
    }

    fun getAdapterLastItem(): BaseBotMessage? {
        return if (messages.isNotEmpty()) messages[messages.size - 1]
        else null
    }

    fun onBrandingDetails() {
        submitList(createRows(emptyList()))
        submitList(createRows(messages))
    }

    @Suppress("UNCHECKED_CAST")
    private fun createRows(botMessages: List<BaseBotMessage>): List<SimpleListRow> {
        var rows = emptyList<SimpleListRow>()
        var currentTimeStamp = ""
        var previousTimeStamp = ""
        for (index in botMessages.indices) {
            val baseBotMsg = botMessages[index]
            val isLastItem = index == botMessages.size - 1
            val msgId = baseBotMsg.messageId
            currentTimeStamp = baseBotMsg.messageDate
            if (currentTimeStamp != previousTimeStamp) {
                rows = rows + TimeStampTemplateRow(msgId, currentTimeStamp)
                previousTimeStamp = currentTimeStamp
            }
            val msgTime = baseBotMsg.formattedTime
            when (baseBotMsg) {
                is BotRequest -> {
                    val body = baseBotMsg.message?.body
                    if (body?.trim()?.isNotEmpty() == true) {
                        rows = rows + createTextTemplate(msgId, true, body, null, isLastItem, msgTime)
                    }
                }

                is BotResponse -> {
                    val iconUrl = baseBotMsg.icon
                    if (baseBotMsg.message.isEmpty()) emptyList<SimpleListRow>()
                    when (val body = baseBotMsg.message[0].cInfo?.body) {
                        is String -> {
                            if (body.isNotEmpty()) rows = rows + createTextTemplate(msgId, false, body, iconUrl, isLastItem, msgTime)
                        }

                        is PayloadOuter -> {
                            var newRows = createCustomTemplate(body.type, baseBotMsg, isLastItem, rows)
                            if (newRows.size != rows.size) {
                                rows = newRows
                                continue
                            }
                            if (!body.text.isNullOrEmpty()) {
                                rows = rows + createTextTemplate(msgId, false, body.text, iconUrl, isLastItem, msgTime)
                                continue
                            }
                            val innerMap = body.payload
                            if (body.type.equals(BotResponseConstants.KEY_TEMPLATE) && innerMap != null) {
                                if (innerMap.containsKey(BotResponseConstants.KEY_TEMPLATE_TYPE)) {
                                    val textMessage = if (innerMap.contains(KEY_TEXT)) {
                                        innerMap[KEY_TEXT]
                                    } else if (innerMap.containsKey(HEADING)) {
                                        innerMap[HEADING]
                                    } else if (innerMap.containsKey(TEXT_MESSAGE)) {
                                        innerMap[TEXT_MESSAGE]
                                    } else ""

                                    val isTextMsg = textMessage != null && textMessage.toString().isNotEmpty()

                                    val templateType = innerMap[BotResponseConstants.KEY_TEMPLATE_TYPE]
                                    newRows = createCustomTemplate(templateType.toString(), baseBotMsg, isLastItem, rows)
                                    if (newRows == rows) {
                                        if (innerMap[BotResponseConstants.CAROUSEL_TYPE] != null) {
                                            newRows =
                                                createCustomTemplate(innerMap[BotResponseConstants.CAROUSEL_TYPE].toString(), baseBotMsg, isLastItem, rows)
                                        } else if (innerMap[PIE_TYPE] != null) {
                                            newRows = createCustomTemplate(innerMap[PIE_TYPE].toString(), baseBotMsg, isLastItem, rows)
                                        } else if (innerMap[DIRECTION] != null) {
                                            newRows = createCustomTemplate(innerMap[DIRECTION].toString(), baseBotMsg, isLastItem, rows)
                                        } else if (innerMap[BotResponseConstants.TABLE_DESIGN] != null) {
                                            newRows =
                                                createCustomTemplate(innerMap[BotResponseConstants.TABLE_DESIGN].toString(), baseBotMsg, isLastItem, rows)
                                        }
                                    }
                                    if (newRows.size != rows.size) {
                                        var msgRow: TextTemplateRow? = null
                                        if (isTextMsg) {
                                            msgRow = createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                        }
                                        rows = if (msgRow == null) newRows else rows + msgRow + newRows.last()
                                        continue
                                    }

                                    when (templateType) {
                                        BotResponseConstants.TEMPLATE_TYPE_BUTTON -> {
                                            if (!innerMap.containsKey(KEY_BUTTONS)) continue
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = rows + ButtonTemplateRow(baseBotMsg.messageId, innerMap, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_LIST -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows =
                                                rows + BotListTemplateRow(baseBotMsg.messageId, innerMap, iconUrl, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_CAROUSEL -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            val items = (innerMap[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>)
                                            rows = rows +
                                                    if (innerMap[BotResponseConstants.CAROUSEL_TYPE] == BotResponseConstants.CAROUSEL_STACKED) {
                                                        CarouselStackedTemplateRow(baseBotMsg.messageId, items, iconUrl, isLastItem, actionEvent)
                                                    } else {
                                                        CarouselTemplateRow(baseBotMsg.messageId, items, iconUrl, isLastItem, actionEvent)
                                                    }
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_BARCHART -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = rows + BarChartTemplateRow(baseBotMsg.messageId, iconUrl, innerMap)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_PIE_CHART -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = rows + PieChartTemplateRow(baseBotMsg.messageId, innerMap)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_LINE_CHART -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = rows + LineChartTemplateRow(baseBotMsg.messageId, iconUrl, innerMap)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_CARD -> {
                                            rows = rows + CardTemplateRow(baseBotMsg.messageId, innerMap, iconUrl, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_ADVANCED_LIST -> {
                                            if (textMessage != null && textMessage.toString().isNotEmpty()) {
                                                rows = rows + createTextTemplate(
                                                    msgId,
                                                    false,
                                                    textMessage.toString(),
                                                    iconUrl,
                                                    isLastItem,
                                                    msgTime
                                                )
                                            }
                                            rows =
                                                rows + AdvanceTemplateRow(baseBotMsg.messageId, innerMap, iconUrl, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_MINI_TABLE -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = rows + MiniTableTemplateRow(baseBotMsg.messageId, iconUrl, innerMap, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_DROPDOWN -> {
                                            rows =
                                                rows + DropDownTemplateRow(
                                                    baseBotMsg.messageId,
                                                    iconUrl,
                                                    innerMap,
                                                    (innerMap[SELECTED_ITEM] as Int?) ?: -1,
                                                    isLastItem,
                                                    this::onSaveState,
                                                    actionEvent
                                                )
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_ADVANCED_MULTI_SELECT -> {
                                            if (innerMap[DISPLAY_LIMIT] == null) {
                                                innerMap[DISPLAY_LIMIT] = (innerMap[BotResponseConstants.LIMIT] as Double).toInt()
                                            }
                                            rows = rows + AdvanceMultiSelectTemplateRow(
                                                msgId,
                                                iconUrl,
                                                innerMap,
                                                innerMap[DISPLAY_LIMIT] as Int,
                                                isLastItem,
                                                innerMap[SELECTED_ITEM] as ArrayList<Map<String, String>>? ?: ArrayList(),
                                                this::onSaveState,
                                                actionEvent
                                            )
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_MULTI_SELECT -> {
                                            rows = rows + MultiSelectTemplateRow(
                                                msgId,
                                                iconUrl,
                                                innerMap[ELEMENTS] as ArrayList<Map<String, String>>,
                                                isLastItem,
                                                innerMap[SELECTED_ITEM] as ArrayList<Map<String, String>>? ?: ArrayList(),
                                                this::onSaveState,
                                                actionEvent
                                            )
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_FEEDBACK -> {
                                            rows = rows + FeedbackTemplateRow(
                                                baseBotMsg.messageId,
                                                iconUrl,
                                                innerMap,
                                                if (innerMap.contains(SELECTED_FEEDBACK)) innerMap[SELECTED_FEEDBACK] as Int else -1,
                                                isLastItem,
                                                this::onSaveState,
                                                actionEvent
                                            )
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_TABLE_LIST -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = rows + TableListTemplateRow(msgId, iconUrl, innerMap, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_FORM -> {
                                            rows = rows + FormTemplateRow(msgId, iconUrl, innerMap, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_LIST_VIEW -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(
                                                    msgId,
                                                    false,
                                                    textMessage.toString(),
                                                    iconUrl,
                                                    isLastItem,
                                                    msgTime
                                                )
                                            }
                                            rows = rows + ListViewTemplateRow(context, msgId, iconUrl, innerMap, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_RADIO_OPTIONS -> {
                                            rows = rows + RadioOptionsTemplateRow(
                                                msgId,
                                                iconUrl,
                                                innerMap,
                                                (innerMap[SELECTED_POSITION] as Int?) ?: -1,
                                                isLastItem,
                                                this::onSaveState,
                                                actionEvent
                                            )
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_CLOCK -> {
                                            rows = rows + ClockTemplateRow(
                                                msgId,
                                                iconUrl,
                                                isLastItem,
                                                innerMap[SELECTED_TIME] as String? ?: context.getString(R.string.default_click_time),
                                                this::onSaveState,
                                                actionEvent
                                            )
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_TABLE -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                            rows = if (innerMap[BotResponseConstants.TABLE_DESIGN] != null) {
                                                when (innerMap[BotResponseConstants.TABLE_DESIGN] as String) {
                                                    BotResponseConstants.TABLE_VIEW_RESPONSIVE -> {
                                                        rows + TableResponsiveRow(baseBotMsg.messageId, iconUrl, innerMap)
                                                    }

                                                    else -> {
                                                        rows + TableTemplateRow(baseBotMsg.messageId, iconUrl, innerMap)
                                                    }
                                                }
                                            } else {
                                                rows + TableTemplateRow(baseBotMsg.messageId, iconUrl, innerMap)
                                            }
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_LIST_WIDGET_2, BotResponseConstants.TEMPLATE_TYPE_LIST_WIDGET -> {
                                            rows = rows + ListWidgetTemplateRow(baseBotMsg.messageId, iconUrl, innerMap, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_RESULTS -> {
                                            rows = rows + ResultsTemplateRow(baseBotMsg.messageId, iconUrl, innerMap, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_ARTICLE -> {
                                            rows = rows + ArticleTemplateRow(baseBotMsg.messageId, innerMap, isLastItem, actionEvent)
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_OTP -> {
                                            if (innerMap[SLIDER_VIEW] as Boolean? == false) {
                                                rows = rows + OtpTemplateRow(baseBotMsg.messageId, iconUrl, innerMap, isLastItem, actionEvent)
                                            }
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_RESET_PIN -> {
                                            if (innerMap[SLIDER_VIEW] as Boolean? == false) {
                                                rows = rows + ResetPinTemplateRow(baseBotMsg.messageId, iconUrl, innerMap, isLastItem, actionEvent)
                                            }
                                        }

                                        BotResponseConstants.TEMPLATE_TYPE_QUICK_REPLIES,
                                        BotResponseConstants.TEMPLATE_TYPE_DATE,
                                        BotResponseConstants.TEMPLATE_TYPE_DATE_RANGE,
                                        BotResponseConstants.TEMPLATE_TYPE_SYSTEM,
                                        BotResponseConstants.TEMPLATE_TYPE_LIVE_AGENT -> {
                                            if (isTextMsg) {
                                                rows = rows + createTextTemplate(msgId, false, textMessage.toString(), iconUrl, isLastItem, msgTime)
                                            }
                                        }

                                        else -> {
                                            rows = rows + createTextTemplate(msgId, false, (templateType ?: body.type).toString(), iconUrl, isLastItem, msgTime)
                                        }
                                    }
                                } else {
                                    rows = rows + createTextTemplate(msgId, false, innerMap[KEY_TEXT].toString(), iconUrl, isLastItem)
                                }
                            } else if (body.type.equals(BotResponseConstants.COMPONENT_TYPE_MESSAGE)) {
                                if (body.payload?.get(BotResponseConstants.AUDIO_URL) != null) {
                                    newRows = createCustomTemplate(COMPONENT_TYPE_AUDIO, baseBotMsg, isLastItem, rows)
                                    if (newRows != rows) {
                                        rows = newRows
                                        continue
                                    }
                                    rows = rows + ImageTemplateRow(baseBotMsg.messageId, iconUrl, body.payload!!, COMPONENT_TYPE_AUDIO, actionEvent)
                                } else if (body.payload?.get(BotResponseConstants.VIDEO_URL) != null) {
                                    newRows = createCustomTemplate(COMPONENT_TYPE_VIDEO, baseBotMsg, isLastItem, rows)
                                    if (newRows != rows) {
                                        rows = newRows
                                        continue
                                    }
                                    rows = rows + VideoTemplateRow(baseBotMsg.messageId, iconUrl, body.payload!!, actionEvent)
                                }
                            } else if (body.type.equals(BotResponseConstants.COMPONENT_TYPE_ERROR)) {
                                if (innerMap != null && innerMap[KEY_TEXT] != null) {
                                    rows = rows + createTextTemplate(
                                        baseBotMsg.messageId, false, innerMap[KEY_TEXT].toString(), iconUrl, isLastItem, msgTime, innerMap[COLOR].toString()
                                    )
                                }
                            } else if (body.type.equals(BotResponseConstants.COMPONENT_TYPE_TEXT)) {
                                if (innerMap != null && innerMap[KEY_TEXT] != null) {
                                    rows = rows + createTextTemplate(
                                        baseBotMsg.messageId, false, innerMap[KEY_TEXT].toString(), iconUrl, isLastItem, msgTime
                                    )
                                }
                            } else {
                                newRows = createCustomTemplate(body.type, baseBotMsg, isLastItem, rows)
                                if (newRows != rows) {
                                    rows = newRows
                                    continue
                                }

                                when (body.type) {
                                    COMPONENT_TYPE_IMAGE, COMPONENT_TYPE_AUDIO -> {
                                        body.payload?.let {
                                            rows = rows + ImageTemplateRow(baseBotMsg.messageId, iconUrl, it, body.type as String, actionEvent)
                                        }
                                        continue
                                    }

                                    COMPONENT_TYPE_VIDEO -> {
                                        rows = rows + VideoTemplateRow(baseBotMsg.messageId, iconUrl, body.payload!!, actionEvent)
                                        continue
                                    }
                                }
                                rows = rows + createTextTemplate(msgId, false, body.type, iconUrl, isLastItem, msgTime)
                            }
                        }
                    }
                }
            }
        }
        return rows
    }

    private fun createCustomTemplate(customTemplateType: String?, botResponse: BotResponse, isLastItem: Boolean, rows: List<SimpleListRow>)
            : List<SimpleListRow> {
        val customTemplates = SDKConfig.getCustomTemplates()
        if (!customTemplateType.isNullOrEmpty() && customTemplates.isEmpty() && !customTemplates.containsKey(customTemplateType)) return rows
        val pair = customTemplates[customTemplateType] ?: return rows
        val rowType = pair.first
        val rowClass = pair.second
        val constructor = rowClass.constructors.find { it.parameters.size == 4 }
        if (constructor == null) {
            LogUtils.e("BotChatAdapter", "Custom template \"$customTemplateType\" constructor parameters are not matching")
            return rows
        }
        val instance: SimpleListRow = (constructor.call(rowType, botResponse, isLastItem, actionEvent)) as SimpleListRow
        instance.setStateListener(this)
        return rows + instance
    }

    override fun onSaveState(messageId: String, value: Any?, key: String) {
        messages = messages.map { botMessage ->
            if (botMessage is BotResponse && botMessage.messageId == messageId) {
                val message = botMessage.message[0]
                val cInfo = message.cInfo
                val body = cInfo?.body as PayloadOuter
                val payload = body.payload
                payload?.let { if (value == null) payload.remove(key) else payload[key] = value }
                botMessage.message[0] = message.copy(cInfo = cInfo.copy(body = body.copy(payload = payload)))
                botMessage
            } else botMessage
        }

        val newRows = createRows(messages)
        submitList(newRows)
    }
}