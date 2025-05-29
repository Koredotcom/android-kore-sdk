package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.ui.R
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.linkify.LinkifyPlugin

class BotListTemplateAdapter(
    private val context: Context,
    private val listItems: List<Map<String, *>>,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<BotListTemplateAdapter.BotListViewHolder>() {
    class BotListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val botItemTitle: TextView = view.findViewById(R.id.bot_list_item_title)
        val botItemSubtitle: TextView = view.findViewById(R.id.bot_list_item_subtitle)
        val botItemButton: TextView = view.findViewById(R.id.bot_list_item_button)
        val botItemRoot: RelativeLayout = view.findViewById(R.id.bot_list_item_root)
        val botListItemImage: ImageView = view.findViewById(R.id.bot_list_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BotListViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.list_template_cell, parent, false)
        return BotListViewHolder(convertView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: BotListViewHolder, position: Int) {
        val listElementsMap: Map<String, *> = listItems[position]
        val title = listElementsMap[KEY_TITLE] as String
        val subTitle = listElementsMap[BotResponseConstants.KEY_SUB_TITLE] as String

        if (listElementsMap[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL] != null) {
            holder.botListItemImage.isVisible = true;
            Glide.with(context).load(listElementsMap[BotResponseConstants.COMPONENT_TYPE_IMAGE_URL])
                .apply(RequestOptions.bitmapTransform(CircleCrop())).into(holder.botListItemImage)
                .onLoadFailed(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_launcher_foreground, context.theme))
        }
        holder.botItemTitle.text = title

        holder.botItemRoot.setOnClickListener {
            try {
                val btn: Map<*, *> = listElementsMap[BotResponseConstants.DEFAULT_ACTION] as Map<*, *>? ?: return@setOnClickListener
                when (btn[BotResponseConstants.TYPE]) {
                    BotResponseConstants.USER_INTENT,
                    BotResponseConstants.URL,
                    BotResponseConstants.WEB_URL -> {
                        actionEvent(BotChatEvent.UrlClick(btn[BotResponseConstants.URL] as String))
                    }

                    else -> {
                        actionEvent(BotChatEvent.SendMessage(title, btn[BotResponseConstants.PAYLOAD] as String))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        holder.botItemSubtitle.isVisible = subTitle.isNotEmpty()

        val markwon = Markwon.builder(holder.itemView.context)
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(HtmlPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .build()

        markwon.setMarkdown(holder.botItemSubtitle, subTitle)

        if (listElementsMap[BotResponseConstants.KEY_BUTTONS] != null) {
            holder.botItemButton.isVisible = true
            val buttons: ArrayList<*> = listElementsMap[BotResponseConstants.KEY_BUTTONS] as ArrayList<*>
            when (buttons.size > 0) {
                true -> {
                    val btn: Map<*, *> = buttons[0] as Map<*, *>? ?: return
                    holder.botItemButton.text = btn[KEY_TITLE] as String
                    holder.botItemButton.setOnClickListener {
                        try {
                            when (btn[BotResponseConstants.TYPE]) {
                                BotResponseConstants.USER_INTENT,
                                BotResponseConstants.URL,
                                BotResponseConstants.WEB_URL -> {
                                    actionEvent(BotChatEvent.UrlClick(btn[BotResponseConstants.URL] as String))
                                }

                                else -> {
                                    actionEvent(BotChatEvent.SendMessage(title, btn[BotResponseConstants.PAYLOAD] as String))
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                else -> holder.botItemButton.text = context.getString(R.string.title)
            }
        } else {
            holder.botItemButton.isVisible = false
        }
    }
}