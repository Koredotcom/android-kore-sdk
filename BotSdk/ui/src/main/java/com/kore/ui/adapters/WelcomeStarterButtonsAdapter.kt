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

class WelcomeStarterButtonsAdapter (private val context: Context, private val type: String?) : RecyclerView.Adapter<WelcomeStarterButtonsAdapter.QuickReplyViewHolder>() {

    private var quickReplyTemplateArrayList: ArrayList<BrandingQuickStartButtonButtonsModel>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickReplyViewHolder {
        val convertView: View = if (type.equals(BotResponseConstants.TEMPLATE_TYPE_LIST, ignoreCase = true)) LayoutInflater.from(context)
            .inflate(R.layout.welcome_quick_buttons_full, parent, false) else LayoutInflater.from(context).inflate(R.layout.welcome_quick_buttons, parent, false)
        return QuickReplyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: QuickReplyViewHolder, position: Int) {
        val quickReplyTemplate: BrandingQuickStartButtonButtonsModel = quickReplyTemplateArrayList!![position]
        holder.quickReplyTitle.text = quickReplyTemplate.title
        holder.quickReplyRoot.setOnClickListener {
//            if (composeFooterInterface != null && invokeGenericWebViewInterface != null)
//            {
            val quickReplyPayload: String
            try {
                val buttonActionModel: BrandingQuickStartButtonActionModel = quickReplyTemplate.action
                if (buttonActionModel.value?.isNotEmpty() == true) {
                    quickReplyPayload = buttonActionModel.value!!
//                        if (BundleConstants.BUTTON_TYPE_POSTBACK == buttonActionModel.type) {
//                            composeFooterInterface.onSendClick(quickReplyTemplate.title, quickReplyPayload, false)
//                        } else if (BundleConstants.BUTTON_TYPE_USER_INTENT == buttonActionModel.type) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(BundleConstants.BUTTON_TYPE_USER_INTENT)
//                        } else if (BundleConstants.BUTTON_TYPE_TEXT == buttonActionModel.type) {
//                            composeFooterInterface.onSendClick(quickReplyTemplate.title, quickReplyPayload, false)
//                        } else if (BundleConstants.BUTTON_TYPE_WEB_URL == buttonActionModel.type
//                            || BundleConstants.BUTTON_TYPE_URL == buttonActionModel.type) {
//                            invokeGenericWebViewInterface.invokeGenericWebView(quickReplyPayload)
//                        } else {
//                            composeFooterInterface.onSendClick(quickReplyTemplate.title, quickReplyPayload, false)
//                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            }
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

//    fun setComposeFooterInterface(composeFooterInterface: ComposeFooterInterface) {
//        this.composeFooterInterface = composeFooterInterface
//    }
//
//    fun setInvokeGenericWebViewInterface(invokeGenericWebViewInterface: InvokeGenericWebViewInterface) {
//        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface
//    }


    class QuickReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quickReplyTitle: TextView
        val quickReplyRoot: RelativeLayout

        init {
            quickReplyTitle = view.findViewById<TextView>(R.id.quick_reply_item_text)
            quickReplyRoot = view.findViewById<RelativeLayout>(R.id.quick_reply_item_root)
        }
    }

}