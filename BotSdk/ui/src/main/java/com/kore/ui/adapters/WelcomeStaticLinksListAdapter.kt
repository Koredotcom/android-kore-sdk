package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonButtonsModel
import com.kore.ui.R

class WelcomeStaticLinksListAdapter(val context: Context, private val arrStaticLinks: ArrayList<BrandingQuickStartButtonButtonsModel>) :
    RecyclerView.Adapter<WelcomeStaticLinksListAdapter.StaticLinkViewHolder>() {
    private var sendMessage: (message: String, payload: String) -> Unit = { _, _ -> }
    private var loadUrl: (url: String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaticLinkViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.welcome_static_links, parent, false)
        return StaticLinkViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: StaticLinkViewHolder, position: Int) {
        val quickReplyTemplate: BrandingQuickStartButtonButtonsModel = arrStaticLinks[position]
        holder.quickReplyTitle.text = quickReplyTemplate.title
        holder.quickReplyDesc.text = quickReplyTemplate.description

        holder.quickReplyRoot.setOnClickListener {
            val action: BrandingQuickStartButtonActionModel = quickReplyTemplate.action
            if (!action.value.isNullOrEmpty()) {
                val quickReplyPayload = action.value ?: return@setOnClickListener

                if (BotResponseConstants.BUTTON_TYPE_POSTBACK == action.type) {
                    sendMessage(quickReplyTemplate.title, quickReplyPayload)
                } else if (BotResponseConstants.BUTTON_TYPE_USER_INTENT == action.type) {
                    loadUrl(BotResponseConstants.BUTTON_TYPE_USER_INTENT)
                } else if (BotResponseConstants.BUTTON_TYPE_TEXT == action.type) {
                    sendMessage(quickReplyTemplate.title, quickReplyPayload)
                } else if (BotResponseConstants.BUTTON_TYPE_WEB_URL == action.type || BotResponseConstants.BUTTON_TYPE_URL == action.type) {
                    loadUrl(quickReplyPayload)
                } else {
                    sendMessage(quickReplyTemplate.title, quickReplyPayload)
                }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return arrStaticLinks.size
    }

    fun setActionEvent(sendMessage: (message: String, payload: String) -> Unit, loadUrl: (url: String) -> Unit) {
        this.sendMessage = sendMessage
        this.loadUrl = loadUrl
    }

    class StaticLinkViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quickReplyRoot: LinearLayout = view.findViewById(R.id.quick_reply_item_root)
        val quickReplyTitle: TextView = view.findViewById(R.id.link_title)
        val quickReplyDesc: TextView = view.findViewById(R.id.link_desc)
    }
}