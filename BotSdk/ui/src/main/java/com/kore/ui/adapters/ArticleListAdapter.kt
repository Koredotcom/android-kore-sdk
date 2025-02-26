package com.kore.ui.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.internal.LinkedTreeMap
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.markdown.MarkdownImageTagHandler
import com.kore.markdown.MarkdownTagHandler
import com.kore.markdown.MarkdownUtil
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.BUTTON_TYPE_POSTBACK
import com.kore.model.constants.BotResponseConstants.BUTTON_TYPE_URL
import com.kore.model.constants.BotResponseConstants.BUTTON_TYPE_WEB_URL
import com.kore.model.constants.BotResponseConstants.ICON
import com.kore.model.constants.BotResponseConstants.KEY_TITLE
import com.kore.model.constants.BotResponseConstants.TEMPLATE_TYPE_BUTTON
import com.kore.model.constants.BotResponseConstants.TYPE
import com.kore.model.constants.BotResponseConstants.URL
import com.kore.ui.R
import com.kore.ui.transforms.RoundedCornersTransform
import com.squareup.picasso.Picasso
import org.apache.commons.lang3.StringEscapeUtils.unescapeHtml3
import org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4

class ArticleListAdapter(
    private val context: Context,
    private val botListModelArrayList: List<HashMap<String, *>>,
    private val isEnabled: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) :
    RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {
    private val roundedCornersTransform: RoundedCornersTransform = RoundedCornersTransform()

    private fun getItem(position: Int): Map<String, *>? {
        return if (position == AdapterView.INVALID_POSITION) {
            null
        } else {
            botListModelArrayList[position]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.article_list_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return if (botListModelArrayList.isNotEmpty()) botListModelArrayList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val botListModel: Map<String, *> = getItem(position) ?: return
        holder.botListItemImage.isVisible = botListModel[ICON] != null
        val icon: String? = botListModel[ICON] as String?
        if (icon != null) {
            try {
                if (icon.contains(",")) {
                    val imageData: String = icon.substring(icon.indexOf(",") + 1)
                    val decodedString = Base64.decode(imageData.toByteArray(), Base64.DEFAULT)
                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    holder.botListItemImage.setImageBitmap(decodedByte)
                } else {
                    Picasso.get().load(icon.toString()).transform(roundedCornersTransform).into(holder.botListItemImage)
                }
            } catch (e: Exception) {
                holder.botListItemImage.isVisible = false
            }
        }

        if ((botListModel[KEY_TITLE] as String?)?.isNotEmpty() == true) {
            var textualContent: String = unescapeHtml4(botListModel[KEY_TITLE].toString().trim())
            textualContent = unescapeHtml3(textualContent.trim())
            textualContent = MarkdownUtil.processMarkDown(textualContent)
            val sequence: CharSequence = Html.fromHtml(
                textualContent.replace("\n", "<br />"),
                MarkdownImageTagHandler(context, holder.tvAction, textualContent), MarkdownTagHandler()
            )
            val strBuilder = SpannableStringBuilder(sequence)

            holder.botListItemTitle.text = strBuilder
            holder.botListItemTitle.movementMethod = null
        }

        if ((botListModel[BotResponseConstants.DESCRIPTION] as String?)?.isNotEmpty() == true) {
            holder.botListItemSubtitle.visibility = View.VISIBLE
            holder.botListItemSubtitle.text = botListModel[BotResponseConstants.DESCRIPTION].toString()
        }

        if (!(botListModel[BotResponseConstants.CREATED_ON] as String?).isNullOrEmpty()) {
            holder.llCreatedOn.visibility = View.VISIBLE
            holder.tvCreatedUpdatedOn.text =
                "${botListModel[BotResponseConstants.CREATED_ON].toString()}\n${botListModel[BotResponseConstants.UPDATED_ON].toString()}"
        }

        if (botListModel[TEMPLATE_TYPE_BUTTON] == null) {
            holder.botListItemButton.isVisible = false
        } else {
            val widgetButton = HashMap(botListModel[TEMPLATE_TYPE_BUTTON] as LinkedTreeMap<String, *>)
            holder.botListItemButton.isVisible = true
            holder.botListItemButton.text = widgetButton[KEY_TITLE].toString()

            holder.botListItemButton.setOnClickListener { v ->
                if (BUTTON_TYPE_WEB_URL == widgetButton[TYPE] || BUTTON_TYPE_URL == widgetButton[TYPE]) {
                    actionEvent(BotChatEvent.UrlClick(widgetButton[URL].toString()))
                } else if (isEnabled && BUTTON_TYPE_POSTBACK == widgetButton[TYPE]) {
                    actionEvent(BotChatEvent.SendMessage(widgetButton[KEY_TITLE].toString(), widgetButton[KEY_TITLE].toString()))
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val botListItemImage: ImageView = view.findViewById(R.id.bot_list_item_image)
        val botListItemTitle: TextView = view.findViewById(R.id.bot_list_item_title)
        val tvAction: TextView = view.findViewById(R.id.tvAction)
        val botListItemSubtitle: TextView = view.findViewById(R.id.bot_list_item_subtitle)
        val tvCreatedUpdatedOn: TextView = view.findViewById(R.id.tvCreatedUpdatedOn)
        val botListItemButton: Button = view.findViewById(R.id.bot_list_item_button)
        val llCreatedOn: LinearLayout = view.findViewById(R.id.llCreatedOn)
    }
}
