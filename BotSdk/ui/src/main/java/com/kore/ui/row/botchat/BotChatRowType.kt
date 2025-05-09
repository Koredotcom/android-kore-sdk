package com.kore.ui.row.botchat

import com.kore.ui.row.SimpleListRow
import com.kore.ui.row.SimpleListViewHolderProvider
import com.kore.ui.row.botchat.advancemultiselect.AdvanceMultiSelectTemplateProvider
import com.kore.ui.row.botchat.article.ArticleTemplateProvider
import com.kore.ui.row.botchat.form.FormTemplateProvider
import com.kore.ui.row.botchat.listview.ListViewTemplateProvider
import com.kore.ui.row.botchat.multiselect.MultiSelectProvider
import com.kore.ui.row.botchat.radiooptions.RadioOptionsTemplateProvider
import com.kore.ui.row.botchat.tablelist.TableListTemplateProvider

sealed class BotChatRowType : SimpleListRow.SimpleListRowType {
    data class RowType(
        val rowType: String,
        override val ordinal: Int,
        override val provider: SimpleListViewHolderProvider<*>
    ) : BotChatRowType()

    companion object {
        const val ROW_REQUEST_MSG_PROVIDER = "RequestMessage"
        const val ROW_RESPONSE_MSG_PROVIDER = "ResponseMessage"
        const val ROW_BUTTON_PROVIDER = "ButtonTemplate"
        const val ROW_LIST_PROVIDER = "ListTemplate"
        const val ROW_CAROUSEL_PROVIDER = "CarouselTemplate"
        const val ROW_BARCHART_PROVIDER = "BarChartTemplate"
        const val ROW_HORIZONTAL_BARCHART_PROVIDER = "HorizontalBarChartTemplate"
        const val ROW_PIE_CHART_PROVIDER = "PieChartTemplate"
        const val ROW_LINE_CHART_PROVIDER = "LineChartTemplate"
        const val ROW_IMAGE_PROVIDER = "ImageTemplate"
        const val ROW_CARD_PROVIDER = "CardTemplate"
        const val ROW_ADVANCE_PROVIDER = "AdvanceTemplate"
        const val ROW_VIDEO_PROVIDER = "VideoTemplate"
        const val ROW_TABLE_PROVIDER = "TableTemplate"
        const val ROW_TABLE_RESPONSIVE_PROVIDER = "TableResponsiveTemplate"
        const val ROW_MINI_TABLE_PROVIDER = "MiniTableTemplate"
        const val ROW_FEEDBACK_PROVIDER = "FeedbackTemplate"
        const val ROW_ADVANCED_MULTI_SELECT_PROVIDER = "AdvancedMultiSelectTemplate"
        const val ROW_DROP_DOWN_PROVIDER = "DropDownTemplate"
        const val ROW_CAROUSEL_STACKED_PROVIDER = "CarouselStackedTemplate"
        const val ROW_LIST_WIDGET_PROVIDER = "ListWidgetTemplate"
        const val ROW_TABLE_LIST_PROVIDER = "TableListTemplate"
        const val ROW_LIST_VIEW_PROVIDER = "ListViewTemplate"
        const val ROW_RADIO_OPTIONS_PROVIDER = "RadioOptionsTemplate"
        const val ROW_CLOCK_PROVIDER = "ClockTemplate"
        const val ROW_FORM_PROVIDER = "FormTemplate"
        const val ROW_TIME_STAMP_PROVIDER = "TimeStampTemplate"
        const val ROW_MULTI_SELECT_PROVIDER = "MultiSelectTemplate"
        const val ROW_RESULTS_PROVIDER = "ResultsTemplate"
        const val ROW_ARTICLE_PROVIDER = "ArticleTemplate"
        const val ROW_OTP_PROVIDER = "OtpValidationTemplate"
        const val ROW_RESET_PIN_PROVIDER = "ResetPinTemplate"
        const val ROW_ANSWER_PROVIDER = "AnswerTemplate"

        private val providers = mutableMapOf<String, SimpleListViewHolderProvider<*>>()

        private val dynamicRowTypes = mutableMapOf<String, RowType>()

        @JvmStatic
        fun createRowType(name: String, provider: SimpleListViewHolderProvider<*>): BotChatRowType {
            if (providers.isEmpty()) prepareRowTypes()
            val existingRowType = dynamicRowTypes[name]
            providers[name] = provider
            val newRowType = RowType(name, existingRowType?.ordinal ?: dynamicRowTypes.size, provider)
            dynamicRowTypes[name] = newRowType
            return newRowType
        }

        fun getAllRowTypes(): List<BotChatRowType> {
            if (dynamicRowTypes.isEmpty()) prepareRowTypes()
            return dynamicRowTypes.values.toList()
        }

        fun clearAllRows() {
            providers.clear()
            dynamicRowTypes.clear()
        }

        fun find(type: Int): RowType? = dynamicRowTypes.values.find { it.ordinal == type }

        internal fun prepareRowTypes() {
            if (providers.isEmpty()) prepareProviders()
            createRowType(ROW_REQUEST_MSG_PROVIDER, providers[ROW_REQUEST_MSG_PROVIDER]!!)
            createRowType(ROW_RESPONSE_MSG_PROVIDER, providers[ROW_RESPONSE_MSG_PROVIDER]!!)
            createRowType(ROW_BUTTON_PROVIDER, providers[ROW_BUTTON_PROVIDER]!!)
            createRowType(ROW_LIST_PROVIDER, providers[ROW_LIST_PROVIDER]!!)
            createRowType(ROW_CAROUSEL_PROVIDER, providers[ROW_CAROUSEL_PROVIDER]!!)
            createRowType(ROW_BARCHART_PROVIDER, providers[ROW_BARCHART_PROVIDER]!!)
            createRowType(ROW_HORIZONTAL_BARCHART_PROVIDER, providers[ROW_HORIZONTAL_BARCHART_PROVIDER]!!)
            createRowType(ROW_PIE_CHART_PROVIDER, providers[ROW_PIE_CHART_PROVIDER]!!)
            createRowType(ROW_LINE_CHART_PROVIDER, providers[ROW_LINE_CHART_PROVIDER]!!)
            createRowType(ROW_IMAGE_PROVIDER, providers[ROW_IMAGE_PROVIDER]!!)
            createRowType(ROW_CARD_PROVIDER, providers[ROW_CARD_PROVIDER]!!)
            createRowType(ROW_ADVANCE_PROVIDER, providers[ROW_ADVANCE_PROVIDER]!!)
            createRowType(ROW_VIDEO_PROVIDER, providers[ROW_VIDEO_PROVIDER]!!)
            createRowType(ROW_TABLE_PROVIDER, providers[ROW_TABLE_PROVIDER]!!)
            createRowType(ROW_TABLE_RESPONSIVE_PROVIDER, providers[ROW_TABLE_RESPONSIVE_PROVIDER]!!)
            createRowType(ROW_MINI_TABLE_PROVIDER, providers[ROW_MINI_TABLE_PROVIDER]!!)
            createRowType(ROW_ADVANCED_MULTI_SELECT_PROVIDER, providers[ROW_ADVANCED_MULTI_SELECT_PROVIDER]!!)
            createRowType(ROW_FEEDBACK_PROVIDER, providers[ROW_FEEDBACK_PROVIDER]!!)
            createRowType(ROW_DROP_DOWN_PROVIDER, providers[ROW_DROP_DOWN_PROVIDER]!!)
            createRowType(ROW_CAROUSEL_STACKED_PROVIDER, providers[ROW_CAROUSEL_STACKED_PROVIDER]!!)
            createRowType(ROW_LIST_WIDGET_PROVIDER, providers[ROW_LIST_WIDGET_PROVIDER]!!)
            createRowType(ROW_TABLE_LIST_PROVIDER, providers[ROW_TABLE_LIST_PROVIDER]!!)
            createRowType(ROW_LIST_VIEW_PROVIDER, providers[ROW_LIST_VIEW_PROVIDER]!!)
            createRowType(ROW_RADIO_OPTIONS_PROVIDER, providers[ROW_RADIO_OPTIONS_PROVIDER]!!)
            createRowType(ROW_CLOCK_PROVIDER, providers[ROW_CLOCK_PROVIDER]!!)
            createRowType(ROW_FORM_PROVIDER, providers[ROW_FORM_PROVIDER]!!)
            createRowType(ROW_TIME_STAMP_PROVIDER, providers[ROW_TIME_STAMP_PROVIDER]!!)
            createRowType(ROW_MULTI_SELECT_PROVIDER, providers[ROW_MULTI_SELECT_PROVIDER]!!)
            createRowType(ROW_RESULTS_PROVIDER, providers[ROW_RESULTS_PROVIDER]!!)
            createRowType(ROW_ARTICLE_PROVIDER, providers[ROW_ARTICLE_PROVIDER]!!)
            createRowType(ROW_OTP_PROVIDER, providers[ROW_OTP_PROVIDER]!!)
            createRowType(ROW_RESET_PIN_PROVIDER, providers[ROW_RESET_PIN_PROVIDER]!!)
            createRowType(ROW_ANSWER_PROVIDER, providers[ROW_ANSWER_PROVIDER]!!)
        }

        fun getRowType(rowType: String): RowType {
            if (dynamicRowTypes.isEmpty()) prepareRowTypes()
            return if (dynamicRowTypes.containsKey(rowType)) {
                dynamicRowTypes[rowType]!!
            } else {
                createRowType(rowType, providers[rowType]!!) as RowType
            }
        }

        private fun prepareProviders() {
            providers[ROW_REQUEST_MSG_PROVIDER] = TextTemplateProvider()
            providers[ROW_RESPONSE_MSG_PROVIDER] = TextTemplateProvider()
            providers[ROW_BUTTON_PROVIDER] = ButtonTemplateProvider()
            providers[ROW_LIST_PROVIDER] = BotListTemplateProvider()
            providers[ROW_CAROUSEL_PROVIDER] = CarouselTemplateProvider()
            providers[ROW_BARCHART_PROVIDER] = BarChartTemplateProvider()
            providers[ROW_HORIZONTAL_BARCHART_PROVIDER] = HorizontalBarChartProvider()
            providers[ROW_PIE_CHART_PROVIDER] = PieChartTemplateProvider()
            providers[ROW_LINE_CHART_PROVIDER] = LineChartTemplateProvider()
            providers[ROW_IMAGE_PROVIDER] = ImageTemplateProvider()
            providers[ROW_CARD_PROVIDER] = CardTemplateProvider()
            providers[ROW_ADVANCE_PROVIDER] = AdvancedListTemplateProvider()
            providers[ROW_VIDEO_PROVIDER] = VideoTemplateProvider()
            providers[ROW_TABLE_PROVIDER] = TableTemplateProvider()
            providers[ROW_TABLE_RESPONSIVE_PROVIDER] = TableResponsiveProvider()
            providers[ROW_MINI_TABLE_PROVIDER] = MiniTableTemplateProvider()
            providers[ROW_ADVANCED_MULTI_SELECT_PROVIDER] = AdvanceMultiSelectTemplateProvider()
            providers[ROW_FEEDBACK_PROVIDER] = FeedbackTemplateProvider()
            providers[ROW_DROP_DOWN_PROVIDER] = DropDownTemplateProvider()
            providers[ROW_CAROUSEL_STACKED_PROVIDER] = CarouselStackedTemplateProvider()
            providers[ROW_LIST_WIDGET_PROVIDER] = ListWidgetTemplateProvider()
            providers[ROW_TABLE_LIST_PROVIDER] = TableListTemplateProvider()
            providers[ROW_LIST_VIEW_PROVIDER] = ListViewTemplateProvider()
            providers[ROW_RADIO_OPTIONS_PROVIDER] = RadioOptionsTemplateProvider()
            providers[ROW_CLOCK_PROVIDER] = ClockTemplateProvider()
            providers[ROW_FORM_PROVIDER] = FormTemplateProvider()
            providers[ROW_TIME_STAMP_PROVIDER] = TimeStampTemplateProvider()
            providers[ROW_MULTI_SELECT_PROVIDER] = MultiSelectProvider()
            providers[ROW_RESULTS_PROVIDER] = ResultsTemplateProvider()
            providers[ROW_ARTICLE_PROVIDER] = ArticleTemplateProvider()
            providers[ROW_OTP_PROVIDER] = OtpTemplateProvider()
            providers[ROW_RESET_PIN_PROVIDER] = ResetPinTemplateProvider()
            providers[ROW_ANSWER_PROVIDER] = AnswerTemplateProvider()
        }
    }
}