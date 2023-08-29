package kore.botssdk.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kore.botssdk.R
import kore.botssdk.listener.BotSocketConnectionManager
import kore.botssdk.models.BaseBotMessage
import kore.botssdk.models.BotRequest
import kore.botssdk.models.BotResponse
import kore.botssdk.models.BotResponseMessage
import kore.botssdk.models.BotResponsePayLoadText
import kore.botssdk.models.ComponentModel
import kore.botssdk.models.PayloadInner
import kore.botssdk.models.PayloadOuter
import kore.botssdk.utils.DateUtils
import kore.botssdk.utils.StringUtils
import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.chatbot.botrequestresponse.BotRequestResponseRow
import kore.botssdk.view.row.chatbot.ChatBotRowType
import kore.botssdk.view.row.chatbot.listwidget.BotListWidgetRow
import java.util.Date

object ChatBotHelper {

    private var LOG_TAG = ChatBotHelper::class.java.simpleName

    var lastMsgId = ""

    fun processPayload(
        context: Context,
        payload: String,
        botLocalResponse: BotResponse?,
        addMessageToChatBotAdapter: (botResponse: BotResponse) -> Unit,
        updateContentListOnSend: (botRequest: BotRequest) -> Unit,
        showQuickReplies: (botResponse: BotResponse?) -> Unit,
        setBotTypingStatus: (botResponse: BotResponse) -> Unit
    ) {
        if (botLocalResponse == null) BotSocketConnectionManager.getInstance().stopDelayMsgTimer()
        val gson = Gson()
        try {
            val botResponse = botLocalResponse ?: gson.fromJson(payload, BotResponse::class.java)
            if (botResponse == null || botResponse.message.isNullOrEmpty()) {
                return
            }
            Log.d(LOG_TAG, payload)
            if (botResponse.message.isNotEmpty()) {
                val compModel = botResponse.message[0].component
                if (compModel != null) {
                    var payOuter = compModel.payload as PayloadOuter
                    payOuter.text?.let {
                        payOuter = if (it.contains("&quot") || it.contains("*")) {
                            gson.fromJson(payOuter.text.replace("&quot;", "\""), PayloadOuter::class.java)
                        } else {
                            payOuter
                        }
                    }
                    val payloadInner: PayloadInner? = payOuter.payload
                    if (payloadInner?.template_type != null && "start_timer".equals(payloadInner.template_type, ignoreCase = true)) {
                        BotSocketConnectionManager.getInstance().startDelayMsgTimer()
                    }
                    setBotTypingStatus(botResponse)
                    payloadInner?.convertElementToAppropriate()
                    if (botResponse.messageId != null) lastMsgId = botResponse.messageId
                    addMessageToChatBotAdapter(botResponse)
                    showQuickReplies(botResponse)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            if (e is JsonSyntaxException) {
                try {
                    //This is the case Bot returning user sent message from another channel
                    val botRequest: BotRequest = gson.fromJson(payload, BotRequest::class.java)
                    botRequest.createdOn = DateUtils.isoFormatter.format(Date())
                    updateContentListOnSend(botRequest)
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    try {
                        val botResponse: BotResponsePayLoadText =
                            gson.fromJson(payload, BotResponsePayLoadText::class.java)
                        if (botResponse.message.isNullOrEmpty()) {
                            return
                        }
                        Log.d(LOG_TAG, payload)
                        if (botResponse.message.isNotEmpty()) {
                            val compModel = botResponse.message[0].component
                            if (compModel != null && !StringUtils.isNullOrEmpty(compModel.payload)) {
                                displayMessage(
                                    compModel.payload,
                                    BotResponse.COMPONENT_TYPE_TEXT,
                                    botResponse.messageId,
                                ) { localPayload, response ->
                                    processPayload(
                                        context,
                                        localPayload,
                                        response,
                                        addMessageToChatBotAdapter,
                                        updateContentListOnSend,
                                        showQuickReplies,
                                        setBotTypingStatus
                                    )
                                }
                            }
                        }
                    } catch (e2: Exception) {
                        e2.printStackTrace()
                    }
                }
            }
        }
    }

    private fun displayMessage(
        text: String,
        type: String,
        messageId: String,
        processPayload: (payload: String, botLocalResponse: BotResponse?) -> Unit
    ) {
        var isDeserializeSuccess = false
        var payloadOuter: PayloadOuter? = null

        if (!lastMsgId.equals(messageId, ignoreCase = true)) {
            try {
                payloadOuter = Gson().fromJson(text, PayloadOuter::class.java)
                if (StringUtils.isNullOrEmpty(payloadOuter.type)) payloadOuter.type = type
                isDeserializeSuccess = true

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            } finally {
                payloadOuter = if (!isDeserializeSuccess) {
                    val payloadInner = PayloadInner()
                    payloadInner.template_type = "text"
                    payloadOuter = PayloadOuter()
                    payloadOuter.text = text
                    payloadOuter.type = "text"
                    payloadOuter.payload = payloadInner
                    payloadOuter
                } else {
                    payloadOuter
                }
                val componentModel = ComponentModel()
                componentModel.type = if (isDeserializeSuccess) payloadOuter?.type else "text"
                componentModel.payload = payloadOuter

                val botResponseMessage = BotResponseMessage()
                botResponseMessage.type = if (isDeserializeSuccess) componentModel.type else "text"
                botResponseMessage.component = componentModel

                val arrBotResponseMessages = java.util.ArrayList<BotResponseMessage>()
                arrBotResponseMessages.add(botResponseMessage)

                val botResponse = BotResponse()
                botResponse.type = if (isDeserializeSuccess) componentModel.type else "text"
                botResponse.message = arrBotResponseMessages
                botResponse.messageId = messageId
                processPayload("", botResponse)
            }
        }
    }

    fun textToSpeech(botResponse: BotResponse) {
        if (botResponse.message.isNullOrEmpty()) return
        var botResponseTextualFormat = ""
        val componentModel = botResponse.message[0].component
        if (componentModel != null) {
            val compType = componentModel.type
            var payOuter = componentModel.payload

            when {
                BotResponse.COMPONENT_TYPE_TEXT.equals(compType, ignoreCase = true) || payOuter.type == null -> {
                    botResponseTextualFormat = payOuter.text
                }

                BotResponse.COMPONENT_TYPE_ERROR.equals(payOuter.type, ignoreCase = true) -> {
                    botResponseTextualFormat = payOuter.payload.text
                }

                BotResponse.COMPONENT_TYPE_TEMPLATE.equals(payOuter.type, ignoreCase = true) ||
                        BotResponse.COMPONENT_TYPE_MESSAGE.equals(payOuter.type, ignoreCase = true) -> {
                    if (payOuter.text != null && payOuter.text.contains("&quot")) {
                        val gson = Gson()
                        payOuter = gson.fromJson(payOuter.text.replace("&quot;", "\""), PayloadOuter::class.java)
                    }
                    val payInner: PayloadInner = payOuter.payload
                    when {
                        payInner.speech_hint != null -> {
                            botResponseTextualFormat = payInner.speech_hint
                        }

                        BotResponse.TEMPLATE_TYPE_BUTTON.equals(payInner.template_type, ignoreCase = true) ||
                                BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equals(payInner.template_type, ignoreCase = true) ||
                                BotResponse.TEMPLATE_TYPE_CAROUSEL.equals(payInner.template_type, ignoreCase = true) ||
                                BotResponse.TEMPLATE_TYPE_CAROUSEL_ADV.equals(payInner.template_type, ignoreCase = true) ||
                                BotResponse.TEMPLATE_TYPE_LIST.equals(payInner.template_type, ignoreCase = true)
                        -> {
                            botResponseTextualFormat = payInner.text
                        }
                    }
                }
            }
        }
        if (BotSocketConnectionManager.getInstance().isTTSEnabled) {
            BotSocketConnectionManager.getInstance().startSpeak(botResponseTextualFormat)
        }
    }

    fun getComponentModel(baseBotMessage: BaseBotMessage): ComponentModel? {
        var compModel: ComponentModel? = null
        if (baseBotMessage is BotResponse && baseBotMessage.message != null && baseBotMessage.message.isNotEmpty()) {
            compModel = baseBotMessage.message[0].component
        }
        return compModel
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getBotResponseRow(context: Context, id: String, msg: String, isLastItem: Boolean): BotRequestResponseRow {
        val sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE)
        val bgColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_BG_COLOR, "#ffffff")
        val textColor = sharedPreferences.getString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, "#000000")
        var drawable = context.getDrawable(R.drawable.theme1_left_bubble_bg) as GradientDrawable
        val themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1)
        if (themeName.equals(BotResponse.THEME_NAME_2, ignoreCase = true)) {
            drawable = context.getDrawable(R.drawable.theme2_left_bubble) as GradientDrawable
        }
        return BotRequestResponseRow(
            ChatBotRowType.ResponseMsg,
            id,
            msg,
            textColor!!,
            bgColor!!,
            drawable,
            false,
            isLastItem
        )
    }

    private fun getListWidgetRows(context: Context, id: String, payloadInner: PayloadInner): List<BotListWidgetRow> {
        var rows = emptyList<BotListWidgetRow>()
        if (payloadInner.widgetlistElements.isEmpty()) return rows
        val sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE)
        val activeButtonTextColor = sharedPreferences.getString(BotResponse.BUTTON_ACTIVE_TXT_COLOR, "#000000")

        for (index in payloadInner.widgetlistElements.indices) {
            if (!payloadInner.isShowMoreClicked && index >= 3) break
            rows = rows + BotListWidgetRow(id, payloadInner.widgetlistElements[index], activeButtonTextColor!!, "", "")
        }

        return rows
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun createRows(context: Context, botMessages: List<BaseBotMessage>): List<SimpleListRow> {
        var rows = emptyList<SimpleListRow>()
        botMessages.mapIndexed { index, baseBotMessage ->
            val isLastItem = index == botMessages.size - 1
            when (baseBotMessage) {
                is BotRequest -> {
                    val sharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE)
                    val bgColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_BG_COLOR, "#C8C8F4")
                    val textColor = sharedPreferences.getString(BotResponse.BUBBLE_RIGHT_TEXT_COLOR, "#121212")
                    val drawable = context.getDrawable(R.drawable.theme1_right_bubble_bg) as GradientDrawable
                    rows = rows + BotRequestResponseRow(
                        ChatBotRowType.RequestMsg,
                        baseBotMessage.formattedDate,
                        baseBotMessage.message.body,
                        textColor!!,
                        bgColor!!,
                        drawable,
                        true
                    )
                }

                is BotResponse -> {
                    val payOuter = baseBotMessage.message[0].component.payload
                    val payInner = payOuter.payload
                    if (BotResponse.COMPONENT_TYPE_TEMPLATE.equals(payOuter.type, ignoreCase = true) && payInner != null) {
                        Log.i("TemplateType", "payInner.getTemplate_type() --->:" + payInner.template_type)
                        if (BotResponse.TEMPLATE_TYPE_BUTTON.equals(payInner.template_type, ignoreCase = true)) {
                            // botButtonView.populateButtonList(payInner.buttons, isLastItem)
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_QUICK_REPLIES.equals(
                                payInner.template_type,
                                ignoreCase = true
                            ) || BotResponse.TEMPLATE_TYPE_FORM_ACTIONS.equals(payInner.template_type, ignoreCase = true)
                        ) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // if (StringUtils.isNullOrEmptyWithTrim(payInner.text)) {
                            // timeStampsTextView.setText("")
                            // }
                        } else if (BotResponse.TEMPLATE_TYPE_CAROUSEL.equals(
                                payInner.template_type,
                                ignoreCase = true
                            ) || BotResponse.TEMPLATE_TYPE_WELCOME_CAROUSEL.equals(payInner.template_type, ignoreCase = true)
                        ) {
                            // botCarouselView.populateCarouselView(payInner.carouselElements, payInner.template_type)
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_LIST.equals(payInner.template_type, ignoreCase = true)) {
                            // botListTemplateView.populateListTemplateView(payInner.listElements, payInner.buttons)
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_PIECHART.equals(payInner.template_type, ignoreCase = true)) {
                            //       bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // val elementModels = payInner.pieChartElements
                            // if (elementModels != null && !elementModels.isEmpty()) {
                            // val xVal = ArrayList<String>(elementModels.size)
                            // val yVal = ArrayList<PieEntry>(elementModels.size)
                            // val arrLables = ArrayList<String>(elementModels.size)
                            // for (i in elementModels.indices) {
                            //     xVal.add(elementModels[i].title)
                            //     yVal.add(PieEntry(elementModels[i].value.toFloat(), " "))
                            //     arrLables.add(elementModels[i].title + " " + elementModels[i].value)
                            // }
                            // botPieChartView.populatePieChart("", payInner.pie_type, xVal, yVal, arrLables)
                            //  }
                        } else if (BotResponse.TEMPLATE_TYPE_TABLE.equals(payInner.template_type, ignoreCase = true)) {
                            if (payInner.tableDesign != null && (payInner.tableDesign.equals(
                                    BotResponse.TABLE_VIEW_RESPONSIVE,
                                    ignoreCase = true
                                ) || payInner.tableDesign.equals("", ignoreCase = true))
                            ) {
                                // bubbleTextMediaLayout.populateText(payInner.text)
                                // responsiveExpandTableView.setData(payInner)
                            } else {
                                // bubbleTextMediaLayout.populateText(payInner.text)
                                // tableView.setData(payInner)
                            }
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.CUSTOM_TABLE_TEMPLATE.equals(payInner.template_type, ignoreCase = true)) {
                            //   bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            //   botCustomTableView.setData(payInner)
                        } else if (BotResponse.TEMPLATE_TYPE_MINITABLE.equals(payInner.template_type, ignoreCase = true)) {
                            //   bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            //   koraCarouselView.populateMiniTable(payInner.template_type, payInner)
                        } else if (BotResponse.TEMPLATE_TYPE_MULTI_SELECT.equals(payInner.template_type, ignoreCase = true)) {
                            //   multiSelectView.populateData(payInner, isLastItem)
                            if (!StringUtils.isNullOrEmpty(payInner.text)) {
                                rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                                //       bubbleTextMediaLayout.populateText(payInner.text)
                            } else if (!StringUtils.isNullOrEmpty(payInner.heading)) {
                                rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.heading, isLastItem)
                                // bubbleTextMediaLayout.populateText(payInner.heading)
                            }
                        } else if (BotResponse.TEMPLATE_TYPE_LINECHART.equals(payInner.template_type, ignoreCase = true)) {
                            //   bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            //   lineChartView.setData(payInner)
                        } else if (BotResponse.TEMPLATE_TYPE_BARCHART.equals(payInner.template_type, ignoreCase = true)) {
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            if (!payInner.isStacked) {
                                if (!BotResponse.BAR_CHART_DIRECTION_VERTICAL.equals(payInner.direction, ignoreCase = true)) {
                                    // barChartView.setData(payInner)
                                } else {
                                    //   horizontalBarChartView.setData(payInner)
                                }
                            } else {
                                // stackedBarChatView.setData(payInner)
                            }
                        } else if (BotResponse.TEMPLATE_TYPE_FORM.equals(payInner.template_type, ignoreCase = true)) {
                            // botFormTemplateView.populateData(payInner, isLastItem)
                            // bubbleTextMediaLayout.populateText(payInner.getHeading());
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.heading, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_LIST_VIEW.equals(payInner.template_type, ignoreCase = true)) {
                            // botListViewTemplateView.populateListTemplateView(
                            // payInner.text,
                            // payInner.moreData,
                            // payInner.listElements,
                            // payInner.buttons,
                            // payInner.moreCount,
                            // payInner.seeMore
                            // )
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_TABLE_LIST.equals(payInner.template_type, ignoreCase = true)) {
                            // botTableListTemplateView.populateListTemplateView(payInner.tableListElements)
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_WELCOME_QUICK_REPLIES.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_NOTIFICATIONS.equals(payInner.template_type, ignoreCase = true)) {
                            // agentTransferTemplateView.populateAgentTemplateView(payInner)
                        } else if (BotResponse.TEMPLATE_TYPE_FEEDBACK.equals(payInner.template_type, ignoreCase = true)) {
                            // feedbackTemplateView.populateData(payInner, true)
                        } else if (BotResponse.TEMPLATE_TYPE_LIST_WIDGET.equals(payInner.template_type, ignoreCase = true)) {
                            // listWidgetView.populateListWidgetData(payInner)
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.title, isLastItem)
                            rows = rows + getListWidgetRows(context, baseBotMessage.messageId, payInner)
                        } else if (BotResponse.TEMPLATE_DROPDOWN.equals(payInner.template_type, ignoreCase = true)) {
                            // botDropDownTemplateView.populateData(payInner)
                        } else if (BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL == payInner.template_type) {
                            // val knowledgeData = payInner.knowledgeDetailModels
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // verticalListView.prepareDataSetAndPopulate(knowledgeData, payInner.template_type, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_KORA_ANNOUNCEMENT_CAROUSAL == payInner.template_type) {
                            // //announcement carousal
                            // val annoucementResModelsData = payInner.announcementResModels
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // verticalListView.prepareDataSetAndPopulate(annoucementResModelsData as ArrayList<*>, payInner.template_type, isLastItem)
                        } else if (BotResponse.CONTACT_CARD_TEMPLATE.equals(payInner.template_type, ignoreCase = true)) {
                            // botContactTemplateView.populateContactTemplateView(payInner.cards, payInner.title)
                        } else if (BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL.equals(payInner.template_type, ignoreCase = true)) {
                            // if (payInner.koraSearchResultsModel != null) verticalListView.prepareDataSetAndPopulate(
                            // payInner.koraSearchResultsModel[0].emails,
                            // BotResponse.TEMPLATE_TYPE_KORA_SEARCH_CAROUSAL,
                            // isLastItem
                            // )
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_AUTO_FORMS.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // if (StringUtils.isNullOrEmptyWithTrim(payInner.text)) {
                            // timeStampsTextView.setText("")
                            // }
                        } else if (BotResponse.TEMPLATE_TYPE_SLOT_PICKER.equals(payInner.template_type, ignoreCase = true)) {
                            // val meetingTemplateModels = payInner.meetingTemplateModels
                            // if (meetingTemplateModels != null && meetingTemplateModels.size > 0) {
                            // meetingSlotsView.populateData(meetingTemplateModels[0], isLastItem)
                            // }
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_MEETING_CONFIRM.equals(payInner.template_type, ignoreCase = true)) {
                            // val meetingTemplateModels = payInner.meetingConfirmationModels
                            // if (meetingTemplateModels != null && meetingTemplateModels.size > 0) {
                            // meetingConfirmationView.populateData(meetingTemplateModels[0])
                            // }
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_TASK_VIEW.equals(
                                payInner.template_type,
                                ignoreCase = true
                            ) || BotResponse.TEMPLATE_TASK_FULLVIEW.equals(payInner.template_type, ignoreCase = true)
                        ) {
                            // val taskTemplateModels = payInner.taskTemplateModels
                            // if (taskTemplateModels != null && taskTemplateModels.size > 0) {
                            // val taskTemplateModel = taskTemplateModels[0]
                            // verticalListView.prepareDataToTasks(
                            //   taskTemplateModel, BotResponse.TEMPLATE_TYPE_TASK_VIEW,
                            //   isLastItem && taskTemplateModel.buttons != null && taskTemplateModel.buttons.size > 0
                            // )
                            // }
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_ATTENDEE_SLOTS.equals(payInner.template_type, ignoreCase = true)) {
                            // val meetingTemplateModels = payInner.attendeeSlotTemplateModels
                            // if (meetingTemplateModels != null && meetingTemplateModels.size > 0) {
                            // attendeeSlotSelectionView.populateData(position, meetingTemplateModels[0], isLastItem)
                            // }
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_CAL_EVENTS.equals(
                                payInner.template_type,
                                ignoreCase = true
                            ) || BotResponse.TEMPLATE_TYPE_CANCEL_EVENT.equals(payInner.template_type, ignoreCase = true)
                        ) {
                            // val calList = payInner.calEventsTemplateModels
                            // if (calList != null && !calList.isEmpty()) {
                            // verticalListView.setCursorDuration(payInner.cursor)
                            // verticalListView.prepareDataSetAndPopulate(calList, payInner.template_type, isLastItem)
                            // }
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_FILES_LOOKUP.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // val fileList = payInner.fileLookupModels
                            // if (fileList != null) verticalListView.prepareDataSetAndPopulate(
                            // fileList,
                            // BotResponse.TEMPLATE_TYPE_FILES_LOOKUP,
                            // isLastItem
                            // )
                        } else if (BotResponse.KA_CONTACT_VIEW.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // val contactInfoModels = payInner.contactInfoModels
                            // if (contactInfoModels != null && contactInfoModels.size > 0) contactInfoView.populateData(contactInfoModels[0])
                        } else if (BotResponse.WELCOME_SUMMARY_VIEW.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // val welcomeSummaryModels = payInner.welcomeSummaryModel
                            // if (welcomeSummaryModels != null && welcomeSummaryModels.size > 0) welcomeSummaryView.populateData(
                            // welcomeSummaryModels[0],
                            // isLastItem
                            // )
                        } else if (BotResponse.TEMPLATE_TYPE_UNIVERSAL_SEARCH.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // universalSearchView.populateData(payInner.universalSearchModels)
                        } else if (BotResponse.KORA_SUMMARY_HELP_VIEW.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            // val summaryModels = payInner.koraSummaryHelpModel
                            // if (summaryModels != null && summaryModels.size > 0) koraSummaryHelpView.populateData(summaryModels[0])
                        } else if (BotResponse.TEMPLATE_TYPE_HIDDEN_DIALOG.equals(payInner.template_type, ignoreCase = true)) {
                            //// hiddenDialog.setVisibility(View.VISIBLE);
                        } else if (BotResponse.NARRATOR_TEXT.equals(payInner.template_type, ignoreCase = true)) {
                            //// bubbleTextMediaLayout.populateText(payInner.getText());
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                            //// ArrayList<NarratorTextModel> narratorModels = payInner.getNarratorTextModel();
                            //// if (narratorModels != null){}
                        } else if (BotResponse.TEMPLATE_BANKING_FEEDBACK.equals(payInner.template_type, ignoreCase = true)) {
                            // bankingFeedbackTemplateView.populateData(payInner, isLastItem)
                        } else if (BotResponse.TEMPLATE_TYPE_CONVERSATION_END.equals(payInner.template_type, ignoreCase = true)) {
                            // timeLineView.setText(
                            // String.format(
                            // "%s %s",
                            // getContext().getString(R.string.conversation_end),
                            // DateUtils.getTimeInAmPm(baseBotMessage.getCreatedInMillis())
                            // )
                            // )
                        } else if (BotResponse.KA_SWITCH_SKILL.equals(payInner.template_type, ignoreCase = true)) {
                            // timeLineView.setText(payInner.text)
                        } else if (BotResponse.COMPONENT_TYPE_ERROR.equals(payInner.template_type, ignoreCase = true)) {
                            // bubbleTextMediaLayout.populateErrorText(payInner.text, payInner.color)
                        } else if (BotResponse.TEMPLATE_TYPE_LIST_WIDGET_LOCATION.equals(payInner.template_type, ignoreCase = true)) {
                            // nearByStockAvailableStoreListView.populateNearByStockAvailableStores(payInner.nearByStockAvailableStores, isLastItem)
                        } else if (BotResponse.TEMPLATE_PROD_INVENTORY_ACTION_FORM.equals(payInner.template_type, ignoreCase = true)) {
                            // if (isLastItem) {
                            //   productInventoryActionFormView.showInputForm(payInner.productInventoryActionFormTitle)
                            // }
                        } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.text)) {
                            if (!BotResponse.TEMPLATE_TYPE_DATE.equals(payInner.template_type, ignoreCase = true)) {
                                rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                                // bubbleTextMediaLayout.populateText(payInner.text)
                            } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.text_message)) {
                                rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text_message, isLastItem)
                                // bubbleTextMediaLayout.populateText(payInner.text_message)
                            }
                        } else if (!StringUtils.isNullOrEmptyWithTrim(payInner.template_type)) {
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.template_type, isLastItem)
                            // bubbleTextMediaLayout.populateText(payInner.template_type)
                        } else if (StringUtils.isNullOrEmptyWithTrim(payOuter.text)) {
                            // timeStampsTextView.setText("")
                        }
                    } else if (BotResponse.COMPONENT_TYPE_MESSAGE.equals(payOuter.type, ignoreCase = true) && payInner != null) {
                        // Log.i("TemplateType", "payOuter.getType() --->:" + payOuter.type)
                        if (!StringUtils.isNullOrEmpty(payInner.videoUrl)) {
                            // imageTemplateView.populateData(payInner, BotResponse.COMPONENT_TYPE_VIDEO)
                        } else if (!StringUtils.isNullOrEmpty(payInner.audioUrl)) {
                            // imageTemplateView.populateData(payInner, BotResponse.COMPONENT_TYPE_AUDIO)
                        } else if (!StringUtils.isNullOrEmpty(payInner.text)) {
                            // bubbleTextMediaLayout.populateText(payInner.text)
                            rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                        }
                    } else if (BotResponse.COMPONENT_TYPE_ERROR.equals(payOuter.type, ignoreCase = true) && payInner != null) {
                        // bubbleTextMediaLayout.populateText(payInner.text)
                        rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payInner.text, isLastItem)
                    } else if ((BotResponse.COMPONENT_TYPE_IMAGE.equals(payOuter.type, ignoreCase = true)
                                || BotResponse.COMPONENT_TYPE_AUDIO.equals(payOuter.type, ignoreCase = true)
                                || BotResponse.COMPONENT_TYPE_VIDEO.equals(payOuter.type, ignoreCase = true)) && payInner != null
                    ) {
                        // imageTemplateView.populateData(payInner, payOuter.type)
                    } else {
                        // bubbleTextMediaLayout.populateText(payOuter.text)
                        rows = rows + getBotResponseRow(context, baseBotMessage.messageId, payOuter.text, isLastItem)
                    }
                }
            }
        }
        return rows
    }
}