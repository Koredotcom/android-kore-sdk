package com.kore.ui.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager.widget.PagerAdapter
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonButtonsModel
import com.kore.ui.R

class WelcomeStaticLinksAdapter(
    val context: Context,
    private val arrStaticLinks: ArrayList<BrandingQuickStartButtonButtonsModel>,
    private val bgColor: String
) : PagerAdapter() {
    private var sendMessage: (message: String, payload: String) -> Unit = { _, _ -> }
    private var loadUrl: (url: String) -> Unit = {}

    override fun getCount(): Int {
        return this@WelcomeStaticLinksAdapter.arrStaticLinks.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val carouselItemLayout: View = LayoutInflater.from(context).inflate(R.layout.welcome_static_links, container, false)
        val staticViewHolder = StaticViewHolder(carouselItemLayout, bgColor)
        val linkItem = arrStaticLinks[position]
        staticViewHolder.linkTitle.text = linkItem.title
        staticViewHolder.linkDesc.text = linkItem.description
        container.addView(carouselItemLayout)

        staticViewHolder.quickReplyView.setOnClickListener {
            val action = linkItem.action
            if (!action.value.isNullOrEmpty()) {
                val quickReplyPayload = action.value ?: return@setOnClickListener

                if (BotResponseConstants.BUTTON_TYPE_POSTBACK == action.type) {
                    sendMessage(linkItem.title, quickReplyPayload)
                } else if (BotResponseConstants.BUTTON_TYPE_USER_INTENT == action.type) {
                    loadUrl(BotResponseConstants.BUTTON_TYPE_USER_INTENT)
                } else if (BotResponseConstants.BUTTON_TYPE_TEXT == action.type) {
                    sendMessage(linkItem.title, quickReplyPayload)
                } else if (BotResponseConstants.BUTTON_TYPE_WEB_URL == action.type || BotResponseConstants.BUTTON_TYPE_URL == action.type) {
                    loadUrl(quickReplyPayload)
                } else {
                    sendMessage(linkItem.title, quickReplyPayload)
                }
            }
        }
        return carouselItemLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    fun setActionEvent(
        sendMessage: (message: String, payload: String) -> Unit,
        loadUrl: (url: String) -> Unit
    ) {
        this.sendMessage = sendMessage
        this.loadUrl = loadUrl
    }

    class StaticViewHolder(itemView: View, bgColor: String) : ViewHolder(itemView) {
        val linkTitle: TextView = itemView.findViewById(R.id.link_title)
        val linkDesc: TextView = itemView.findViewById(R.id.link_desc)
        val quickReplyView: RelativeLayout = itemView.findViewById(R.id.quick_reply_view)

        init {
            val gradientDrawable: GradientDrawable? = quickReplyView.background as GradientDrawable?
            gradientDrawable?.setStroke((1.dpToPx(itemView.context)), bgColor.toColorInt())
        }
    }
}