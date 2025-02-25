package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.gson.internal.LinkedTreeMap
import com.kore.common.event.UserActionEvent
import com.kore.model.constants.BotResponseConstants.ANSWER
import com.kore.model.constants.BotResponseConstants.ANSWER_PAYLOAD
import com.kore.model.constants.BotResponseConstants.CENTER_PANEL
import com.kore.model.constants.BotResponseConstants.DATA
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.SNIPPET_CONTENT
import com.kore.model.constants.BotResponseConstants.SOURCES
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.ui.adapters.AnswerSourceAdapter
import com.kore.ui.audiocodes.webrtcclient.general.Log
import com.kore.ui.databinding.RowAnswerTemplateBinding
import com.kore.ui.row.SimpleListRow

class AnswerTemplateRow(
    private val id: String,
    private val payload: HashMap<String, Any>,
    private val actionEvent: (actionEvent: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_ANSWER_PROVIDER)
    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is AnswerTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        return otherRow is AnswerTemplateRow
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any = true

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, "", false, true)
        val childBinding = RowAnswerTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            answerContent.text = payload[ANSWER] as String?
            val sourceLinks = HashMap<String, String>()
            (payload[ANSWER_PAYLOAD] as LinkedTreeMap<String, Any>?)?.let { answerPayload ->
                (answerPayload[CENTER_PANEL] as LinkedTreeMap<String, Any>?)?.let { centerPanel ->
                    (centerPanel[DATA] as ArrayList<LinkedTreeMap<String, Any>>?)?.let { data ->
                        data.map { dataItem ->
                            (dataItem[SNIPPET_CONTENT] as ArrayList<LinkedTreeMap<String, Any>>?)?.let { snippetContent ->
                                snippetContent.map { snippetItem ->
                                    (snippetItem[SOURCES] as ArrayList<LinkedTreeMap<String, Any>>?)?.let { sources ->
                                        sources.map { source ->
                                            if (!(source[KEY_TITLE] as String?).isNullOrEmpty() && !(source[URL] as String?).isNullOrEmpty()) {
                                                sourceLinks[source[KEY_TITLE].toString()] = source[URL].toString()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                linksRecycler.adapter = AnswerSourceAdapter(sourceLinks, actionEvent)
            }
            Log.e("Called", "sourceLinks " + sourceLinks.size)
        }
    }
}