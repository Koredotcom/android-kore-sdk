package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.network.api.responsemodels.branding.PromotionsModel
import com.kore.ui.R

class WelcomePromotionsAdapter(val context: Context, private val promotions: ArrayList<PromotionsModel>) :
    BaseAdapter() {
    private var sendMessage: (message: String, payload: String) -> Unit = { _, _ -> }
    private var loadUrl: (url: String) -> Unit = {}

    override fun getCount(): Int {
        return promotions.size
    }

    override fun getItem(position: Int): Any {
        return promotions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.promotions_banner, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        val media = promotions[position].banner
        val promoItem = promotions[position]

        Glide.with(context)
            .load(media)
            .into(vh.ivPromotions)
            .onLoadFailed(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_launcher_foreground, context.theme))

        vh.itemView.setOnClickListener {
            try {
                val buttonActionModel: BrandingQuickStartButtonActionModel = promoItem.action
                if (buttonActionModel.value?.isNotEmpty() == true) {
                    val quickReplyPayload = buttonActionModel.value
                    if (BotResponseConstants.BUTTON_TYPE_POSTBACK == buttonActionModel.type) {
                        buttonActionModel.title?.let { it1 ->
                            if (quickReplyPayload != null) sendMessage(it1, quickReplyPayload)
                        }
                    } else if (BotResponseConstants.BUTTON_TYPE_USER_INTENT == buttonActionModel.type) {
                        loadUrl(BotResponseConstants.BUTTON_TYPE_USER_INTENT)
                    } else if (BotResponseConstants.BUTTON_TYPE_TEXT == buttonActionModel.type) {
                        buttonActionModel.title?.let { it1 ->
                            if (quickReplyPayload != null) sendMessage(it1, quickReplyPayload)
                        }
                    } else if (BotResponseConstants.BUTTON_TYPE_WEB_URL == buttonActionModel.type
                        || BotResponseConstants.BUTTON_TYPE_URL == buttonActionModel.type
                    ) {
                        if (quickReplyPayload != null) loadUrl(quickReplyPayload)
                    } else {
                        buttonActionModel.title?.let { it1 ->
                            if (quickReplyPayload != null) sendMessage(it1, quickReplyPayload)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return view
    }

    fun setActionEvent(
        sendMessage: (message: String, payload: String) -> Unit,
        loadUrl: (url: String) -> Unit
    ) {
        this.sendMessage = sendMessage
        this.loadUrl = loadUrl
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPromotions: ImageView = itemView.findViewById(R.id.ivPromotionsBanner)
    }
}