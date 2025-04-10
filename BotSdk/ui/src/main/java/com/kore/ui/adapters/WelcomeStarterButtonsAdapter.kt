package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonButtonsModel
import com.kore.ui.R

class WelcomeStarterButtonsAdapter(private val context: Context, private val type: String?) :
    RecyclerView.Adapter<WelcomeStarterButtonsAdapter.QuickReplyViewHolder>() {

    private var quickReplyTemplateArrayList: ArrayList<BrandingQuickStartButtonButtonsModel>? = null
    private var sendMessage: (message: String, payload: String) -> Unit = { _, _ -> }
    private var loadUrl: (url: String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickReplyViewHolder {
        val convertView: View = if (type.equals(BotResponseConstants.TEMPLATE_TYPE_LIST, ignoreCase = true)) LayoutInflater.from(context)
            .inflate(R.layout.welcome_quick_buttons_full, parent, false) else LayoutInflater.from(context)
            .inflate(R.layout.welcome_quick_buttons, parent, false)
        return QuickReplyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: QuickReplyViewHolder, position: Int) {
        val quickReplyTemplate: BrandingQuickStartButtonButtonsModel = quickReplyTemplateArrayList!![position]
        holder.quickReplyTitle.text = quickReplyTemplate.title
        holder.quickReplyRoot.setOnClickListener {
            val quickReplyPayload: String
            try {
                val buttonActionModel: BrandingQuickStartButtonActionModel = quickReplyTemplate.action
                if (buttonActionModel.value?.isNotEmpty() == true) {
                    quickReplyPayload = buttonActionModel.value!!
                    if (BotResponseConstants.BUTTON_TYPE_POSTBACK == buttonActionModel.type) {
                        sendMessage(quickReplyTemplate.title, quickReplyPayload)
                    } else if (BotResponseConstants.BUTTON_TYPE_USER_INTENT == buttonActionModel.type) {
                        loadUrl(BotResponseConstants.BUTTON_TYPE_USER_INTENT)
                    } else if (BotResponseConstants.BUTTON_TYPE_TEXT == buttonActionModel.type) {
                        sendMessage(quickReplyTemplate.title, quickReplyPayload)
                    } else if (BotResponseConstants.BUTTON_TYPE_WEB_URL == buttonActionModel.type
                        || BotResponseConstants.BUTTON_TYPE_URL == buttonActionModel.type
                    ) {
                        loadUrl(quickReplyPayload)
                    } else {
                        sendMessage(quickReplyTemplate.title, quickReplyPayload)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return quickReplyTemplateArrayList?.size ?: 0
    }

    fun setWelcomeStarterButtonsArrayList(quickReplyTemplateArrayList: ArrayList<BrandingQuickStartButtonButtonsModel>) {
        this@WelcomeStarterButtonsAdapter.quickReplyTemplateArrayList = quickReplyTemplateArrayList
    }

    fun setActionEvent(
        sendMessage: (message: String, payload: String) -> Unit,
        loadUrl: (url: String) -> Unit
    ) {
        this.sendMessage = sendMessage
        this.loadUrl = loadUrl
    }

    class QuickReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quickReplyTitle: TextView = view.findViewById(R.id.quick_reply_item_text)
        val quickReplyRoot: RelativeLayout = view.findViewById(R.id.quick_reply_item_root)
    }
}