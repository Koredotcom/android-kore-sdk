package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.model.constants.BotResponseConstants.CENTER_PANEL
import com.kore.model.constants.BotResponseConstants.DATA
import com.kore.model.constants.BotResponseConstants.GRAPH_ANSWER
import com.kore.model.constants.BotResponseConstants.PAYLOAD
import com.kore.model.constants.BotResponseConstants.RESULTS
import com.kore.model.constants.BotResponseConstants.WEB
import com.kore.ui.adapters.LiveSearchCycleAdapter
import com.kore.ui.adapters.SearchCentralPanelAdapter
import com.kore.ui.databinding.RowResultsTemplateBinding

class ResultsTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val actionEvent: (event: UserActionEvent) -> Unit,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_RESULTS_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ResultsTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is ResultsTemplateRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowResultsTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            lvLiveSearch.layoutManager = LinearLayoutManager(root.context)
            lvCentralPanel.layoutManager = LinearLayoutManager(root.context)

            val graphAnswer: HashMap<String, Any>? = payload[GRAPH_ANSWER] as HashMap<String, Any>?
            val payloadInner: HashMap<String, Any>? = graphAnswer?.get(PAYLOAD) as HashMap<String, Any>?
            val centralPanel: HashMap<String, Any>? = payloadInner?.get(CENTER_PANEL) as HashMap<String, Any>?
            val data: ArrayList<HashMap<String, Any>>? = centralPanel?.get(DATA) as ArrayList<HashMap<String, Any>>?

            if (graphAnswer != null && payloadInner != null && centralPanel != null && !data.isNullOrEmpty()) {
                val panelAdapter = SearchCentralPanelAdapter(root.context, data, actionEvent)
                lvCentralPanel.adapter = panelAdapter
            } else {
                llLiveSearch.isVisible = false
                tvSeeAllResults.isVisible = false
                tvPageTitle.isVisible = true
                llResults.isVisible = true
            }

            val results: HashMap<String, Any>? = payload[RESULTS] as HashMap<String, Any>?
            val webResults: HashMap<String, Any>? = results?.get(WEB) as HashMap<String, Any>?
            val webData: ArrayList<HashMap<String, Any>>? = webResults?.get(WEB) as ArrayList<HashMap<String, Any>>?

            if (results != null && webResults != null && webData != null) {
                val liveSearchCycleAdapter = LiveSearchCycleAdapter(webData, actionEvent)
                lvLiveSearch.adapter = liveSearchCycleAdapter
            }

            tvSeeAllResults.setOnClickListener {
                tvSeeAllResults.isVisible = false
                tvPageTitle.isVisible = true
                llResults.isVisible = true
            }
        }
    }
}