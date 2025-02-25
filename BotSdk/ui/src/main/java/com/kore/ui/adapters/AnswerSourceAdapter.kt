package com.kore.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.ui.R
import com.kore.ui.textview.CustomTextView

class AnswerSourceAdapter(
    private val sources: HashMap<String, String>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<AnswerSourceAdapter.SourceViewHolder>() {

    class SourceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rootItem: CustomTextView = view.findViewById(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        return SourceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.answer_source_item, parent, false))
    }

    override fun getItemCount(): Int = sources.size

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        val data = holder.rootItem.context.getString(R.string.source_item).format((position + 1), sources.keys.elementAt(position))
        holder.rootItem.text = data
        holder.rootItem.setOnClickListener {
            actionEvent(BotChatEvent.UrlClick(sources[sources.keys.elementAt(position)].toString()))
        }
    }
}
