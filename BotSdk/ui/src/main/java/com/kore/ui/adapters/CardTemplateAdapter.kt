package com.kore.ui.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.extensions.dpToPx
import com.kore.ui.utils.BitmapUtils
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.LARGE
import com.kore.model.constants.BotResponseConstants.SMALL
import com.kore.ui.R

class CardTemplateAdapter(
    val context: Context,
    private val arrCardTemplateModels: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<CardTemplateAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_template_cell, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardTemplateModel: Map<String, *> = arrCardTemplateModels[position]
        val popUpView = View.inflate(context, R.layout.advancelist_drop_down_popup, null)
        val popupWindow = PopupWindow(popUpView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        holder.botListItemSubtitle.isVisible = false
        holder.botListItemImage.isVisible = false
        holder.tvOnlyTitle.isVisible = true
        holder.rlTitle.isVisible = false
        holder.tvCardButton.isVisible = false
        holder.rvDescription.isVisible = false
        holder.vBorder.isVisible = false

        if (cardTemplateModel[BotResponseConstants.CARD_HEADING] != null) {
            val cardHeading = cardTemplateModel[BotResponseConstants.CARD_HEADING] as Map<String, *>
            holder.tvOnlyTitle.isVisible = true
            holder.tvOnlyTitle.text = cardHeading[BotResponseConstants.KEY_TITLE] as String
            if (cardHeading[BotResponseConstants.DESCRIPTION] != null) {
                holder.tvOnlyTitle.isVisible = false
                holder.rlTitle.isVisible = true
                holder.botListItemTitle.text = cardHeading[BotResponseConstants.KEY_TITLE] as String
                holder.botListItemSubtitle.visibility = View.VISIBLE
                holder.botListItemSubtitle.text = cardHeading[BotResponseConstants.DESCRIPTION] as String
            }
            if (cardHeading[BotResponseConstants.ICON] != null) {
                holder.botListItemImage.isVisible = true
                val layoutParams = RelativeLayout.LayoutParams(100, 100)
                holder.botListItemImage.layoutParams = layoutParams
                if (cardHeading[BotResponseConstants.ICON_SIZE] != null) {
                    when ((cardHeading[BotResponseConstants.ICON_SIZE] as String)) {
                        LARGE -> {
                            layoutParams.height = 200
                            layoutParams.width = 200
                            holder.botListItemImage.layoutParams = layoutParams
                        }

                        SMALL -> {
                            layoutParams.height = 50
                            layoutParams.width = 50
                            holder.botListItemImage.layoutParams = layoutParams
                        }
                    }
                }
                try {
                    val imageData = cardHeading[BotResponseConstants.ICON] as String
                    BitmapUtils.loadImage(context, imageData, holder.botListItemImage, R.drawable.ic_image_photo)
                } catch (e: Exception) {
                    holder.botListItemImage.visibility = View.GONE
                }
            }
            if (cardHeading[BotResponseConstants.HEADER_STYLES] != null) {
                val headerStyles = cardHeading[BotResponseConstants.HEADER_STYLES] as Map<String, *>
                holder.tvOnlyTitle.setTextColor(Color.parseColor(headerStyles[BotResponseConstants.COLOR] as String))
                val rightDrawable = ContextCompat.getDrawable(context, R.drawable.card_title_bg) as GradientDrawable?
                if (rightDrawable != null) {
                    rightDrawable.setColor(Color.parseColor(headerStyles[BotResponseConstants.BG_COLOR] as String))
                    if (headerStyles[BotResponseConstants.BORDER] != null) {
                        val border: List<String> = (headerStyles[BotResponseConstants.BORDER] as String).split("#")
                        if (border.isNotEmpty()) rightDrawable.setStroke((1.dpToPx(context)).toInt(), Color.parseColor("#" + border[1]))
                    } else rightDrawable.setStroke((1.dpToPx(context)).toInt(), Color.parseColor("#00000000"))
                }
                holder.tvOnlyTitle.background = rightDrawable
                headerStyles[BotResponseConstants.FONT_WEIGHT]?.let {
                    when (it as String) {
                        BotResponseConstants.BOLD -> holder.tvOnlyTitle.typeface = Typeface.DEFAULT_BOLD
                    }
                }
            }

            if (cardHeading[BotResponseConstants.HEADER_EXTRA_INFO] != null) {
                val headerOptions = cardHeading[BotResponseConstants.HEADER_EXTRA_INFO] as Map<String, *>
                if (headerOptions[BotResponseConstants.KEY_TITLE] != null) {
                    holder.tvHeaderExtraTitle.isVisible = true
                    holder.tvHeaderExtraTitle.text = headerOptions[BotResponseConstants.KEY_TITLE] as String
                }
                if (headerOptions[BotResponseConstants.ICON] != null) {
                    holder.ivHeaderExtra.isVisible = true
                    try {
                        val imageData = headerOptions[BotResponseConstants.ICON] as String
                        BitmapUtils.loadImage(context, imageData, holder.ivHeaderExtra, R.drawable.ic_image_photo)
                    } catch (e: Exception) {
                        holder.ivHeaderExtra.isVisible = false
                    }
                }
                if (headerOptions[BotResponseConstants.TYPE] != null &&
                    ((headerOptions[BotResponseConstants.TYPE] as String) == BotResponseConstants.DROP_DOWN) &&
                    headerOptions[BotResponseConstants.DROP_DOWN_OPTIONS] != null &&
                    (headerOptions[BotResponseConstants.DROP_DOWN_OPTIONS] as List<Map<String, *>>).isNotEmpty()
                ) {
                    val recyclerView = popUpView!!.findViewById<RecyclerView>(R.id.rvDropDown)
                    val ivDropDownCLose = popUpView.findViewById<ImageView>(R.id.ivDropDownCLose)
                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    val advanceListButtonAdapter = AdvanceListButtonAdapter(
                        context,
                        BotResponseConstants.FULL_WIDTH,
                        headerOptions[BotResponseConstants.DROP_DOWN_OPTIONS] as List<Map<String, *>>,
                        0,
                        isLastItem,
                        actionEvent
                    )
                    recyclerView.adapter = advanceListButtonAdapter
                    ivDropDownCLose.setOnClickListener { popupWindow.dismiss() }
                    holder.ivHeaderExtra.setOnClickListener { popupWindow.showAsDropDown(holder.tvHeaderExtraTitle, -170, 0) }
                }
            } else {
                holder.tvHeaderExtraTitle.isVisible = false
                holder.ivHeaderExtra.isVisible = false
            }
        }

        if (cardTemplateModel[BotResponseConstants.CARD_DESCRIPTION] != null) {
            holder.rvDescription.visibility = View.VISIBLE
            holder.rvDescription.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            if (cardTemplateModel[BotResponseConstants.CARD_TYPE] != null &&
                (cardTemplateModel[BotResponseConstants.CARD_TYPE] as String) == BotResponseConstants.LIST
            ) {
                holder.rvDescription.layoutManager = GridLayoutManager(context, 3)
            }
            holder.rvDescription.adapter =
                CardTemplateListAdapter(context, cardTemplateModel[BotResponseConstants.CARD_DESCRIPTION] as List<Map<String, *>>)
        }

        if (cardTemplateModel[BotResponseConstants.KEY_BUTTONS] != null) {
            val buttons = cardTemplateModel[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>
            if (buttons.isNotEmpty() && buttons.size == 1) {
                val button = buttons[0]
                holder.tvCardButton.visibility = View.VISIBLE
                holder.tvCardButton.text = button[BotResponseConstants.KEY_TITLE] as String
                holder.tvCardButton.setOnClickListener {
                    val listElementButtonPayload = if (button[BotResponseConstants.PAYLOAD] != null) {
                        button[BotResponseConstants.PAYLOAD] as String
                    } else ""

                    val listElementButtonTitle = if (button[BotResponseConstants.KEY_TITLE] != null) {
                        button[BotResponseConstants.KEY_TITLE] as String
                    } else ""
                    try {
                        when (button[BotResponseConstants.TYPE]) {
                            BotResponseConstants.USER_INTENT,
                            BotResponseConstants.URL,
                            BotResponseConstants.WEB_URL -> {
                                if (button[BotResponseConstants.URL] != null) {
                                    actionEvent(BotChatEvent.UrlClick(button[BotResponseConstants.URL] as String))
                                }
                            }

                            BotResponseConstants.POSTBACK -> {
                                if (!isLastItem) return@setOnClickListener
                                actionEvent(BotChatEvent.SendMessage(listElementButtonTitle, listElementButtonPayload))
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (button[BotResponseConstants.BUTTON_STYLES] != null) {
                    val buttonStyle = button[BotResponseConstants.BUTTON_STYLES] as Map<String, *>
                    val rightDrawable = ContextCompat.getDrawable(context, R.drawable.card_btn_bg) as GradientDrawable?
                    if (rightDrawable != null) {
                        if (buttonStyle[BotResponseConstants.BACKGROUND_COLOR] != null) {
                            rightDrawable.setColor(Color.parseColor(buttonStyle[BotResponseConstants.BACKGROUND_COLOR] as String))
                        } else {
                            rightDrawable.setColor(Color.parseColor("#ffffff"))
                        }
                        if (buttonStyle[BotResponseConstants.BORDER] != null) {
                            val border: List<String> = (buttonStyle[BotResponseConstants.BORDER] as String).split("#")
                            if (border.isNotEmpty()) rightDrawable.setStroke(
                                1.dpToPx(context),
                                Color.parseColor("#" + border[1].replace(";", ""))
                            )
                        } else rightDrawable.setStroke(1.dpToPx(context), Color.parseColor("#00000000"))
                    }
                    holder.tvCardButton.background = rightDrawable
                }
            }
        }

        if (cardTemplateModel[BotResponseConstants.CARD_STYLES] != null) {
            val rightDrawable = ContextCompat.getDrawable(context, R.drawable.card_template_top_bg) as GradientDrawable?
            val cardStyles = cardTemplateModel[BotResponseConstants.CARD_STYLES] as Map<String, *>
            if (rightDrawable != null) {
                if (cardStyles[BotResponseConstants.BORDER_LEFT] != null) {
                    val border: List<String> = (cardStyles[BotResponseConstants.BORDER_LEFT] as String).split(" ")
                    if (border.size > 1) {
                        rightDrawable.setColor(Color.parseColor(border[2].replace(";", "")))
                        rightDrawable.setStroke(1.dpToPx(context), Color.parseColor(border[2].replace(";", "")))
                    }
                } else if (cardStyles[BotResponseConstants.BORDER_RIGHT] != null) {
                    val border: List<String> = (cardStyles[BotResponseConstants.BORDER_RIGHT] as String).split(" ")
                    if (border.size > 1) {
                        rightDrawable.setColor(Color.parseColor(border[2].replace(";", "")))
                        rightDrawable.setStroke(1.dpToPx(context), Color.parseColor(border[2].replace(";", "")))
                    }
                    val buttonLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 15, 0)
                    holder.llCardView.layoutParams = buttonLayoutParams
                } else if (cardStyles[BotResponseConstants.BORDER_TOP] != null) {
                    val border: List<String> = (cardStyles[BotResponseConstants.BORDER_TOP] as String).split(" ")
                    if (border.size > 1) {
                        rightDrawable.setColor(Color.parseColor(border[2].replace(";", "")))
                        rightDrawable.setStroke(1.dpToPx(context), Color.parseColor(border[2].replace(";", "")))
                    }
                    val buttonLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 15, 0, 0)
                    holder.llCardView.layoutParams = buttonLayoutParams
                } else if (cardStyles[BotResponseConstants.BORDER_BOTTOM] != null) {
                    val border: List<String> = (cardStyles[BotResponseConstants.BORDER_BOTTOM] as String).split(" ")
                    if (border.size > 1) {
                        rightDrawable.setColor(Color.parseColor(border[2].replace(";", "")))
                        rightDrawable.setStroke(1.dpToPx(context), Color.parseColor(border[2].replace(";", "")))
                    }
                    val buttonLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    buttonLayoutParams.setMargins(0, 0, 0, 15)
                    holder.llCardView.layoutParams = buttonLayoutParams
                } else rightDrawable.setStroke((1.dpToPx(context)), Color.parseColor("#00000000"))
            }
            val buttonLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            buttonLayoutParams.setMargins(0, 10, 0, 0)
            holder.llCardViewTop.layoutParams = buttonLayoutParams
            holder.llCardViewTop.background = rightDrawable
            val templateBb = ContextCompat.getDrawable(context, R.drawable.card_template_bg) as GradientDrawable?
            if (templateBb != null) {
                templateBb.setColor(Color.parseColor("#ffffff"))
                if (cardStyles[BotResponseConstants.BACKGROUND_COLOR] != null) {
                    templateBb.setColor(Color.parseColor(cardStyles[BotResponseConstants.BACKGROUND_COLOR] as String))
                }
                templateBb.setStroke(1.dpToPx(context), Color.parseColor("#84959B"))
            }
            holder.llCardView.background = templateBb
        } else {
            val rightDrawable = ContextCompat.getDrawable(context, R.drawable.card_template_top_bg) as GradientDrawable?
            if (rightDrawable != null) {
                rightDrawable.setColor(Color.parseColor("#ffffff"))
                rightDrawable.setStroke(1.dpToPx(context), Color.parseColor("#00000000"))
                holder.llCardViewTop.background = rightDrawable
            }
            val buttonLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            buttonLayoutParams.setMargins(0, 10, 0, 0)
            holder.llCardView.layoutParams = buttonLayoutParams
        }

        if (cardTemplateModel[BotResponseConstants.CARD_CONTENT_STYLES] != null) {
            val rightDrawable = ContextCompat.getDrawable(context, R.drawable.card_template_bg) as GradientDrawable?
            val cardContentStyle = cardTemplateModel[BotResponseConstants.CARD_CONTENT_STYLES] as Map<String, *>
            if (rightDrawable != null) {
                if (cardContentStyle[BotResponseConstants.BACKGROUND_COLOR] != null)
                    rightDrawable.setColor(Color.parseColor(cardContentStyle[BotResponseConstants.BACKGROUND_COLOR] as String))
                else
                    rightDrawable.setColor(Color.parseColor("#ffffff"))
                if (cardContentStyle[BotResponseConstants.BORDER] != null) {
                    val border: List<String> = (cardContentStyle[BotResponseConstants.BORDER] as String).split("#")
                    if (border.isNotEmpty()) {
                        rightDrawable.setStroke(1.dpToPx(context), Color.parseColor("#" + border[1].replace(";", "")))
                    }
                } else rightDrawable.setStroke(1.dpToPx(context), Color.parseColor("#84959B"))
            }
            if (cardContentStyle[BotResponseConstants.BORDER_LEFT] != null) {
                val border: List<String> = (cardContentStyle[BotResponseConstants.BORDER_LEFT] as String).split(" ")
                holder.vBorder.visibility = View.VISIBLE
                val viewDrawable = ContextCompat.getDrawable(context, R.drawable.card_view_bg) as GradientDrawable?
                if (viewDrawable != null && border.size > 1) {
                    viewDrawable.setColor(Color.parseColor(border[2].replace(";", "")))
                }
                holder.vBorder.background = viewDrawable
            } else {
                holder.vBorder.visibility = View.GONE
            }
            val buttonLayoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            buttonLayoutParams.setMargins(0, 10, 0, 0)
            holder.llCardView.layoutParams = buttonLayoutParams
            holder.llCardView.background = rightDrawable
        } else {
            val rightDrawable = ContextCompat.getDrawable(context, R.drawable.card_template_bg) as GradientDrawable?
            if (rightDrawable != null) {
                rightDrawable.setColor(Color.parseColor("#ffffff"))
                if (cardTemplateModel[BotResponseConstants.CARD_STYLES] != null) {
                    val cardStyle = cardTemplateModel[BotResponseConstants.CARD_STYLES] as Map<String, *>
                    if (cardStyle[BotResponseConstants.BACKGROUND_COLOR] != null)
                        rightDrawable.setColor(Color.parseColor(cardStyle[BotResponseConstants.BACKGROUND_COLOR] as String))
                }
                rightDrawable.setStroke(1.dpToPx(context), Color.parseColor("#84959B"))
                holder.llCardView.background = rightDrawable
            }
        }
    }

    override fun getItemCount(): Int {
        return arrCardTemplateModels.size
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val botListItemImage: ImageView
        val ivHeaderExtra: ImageView
        val botListItemTitle: TextView
        val botListItemSubtitle: TextView
        val tvOnlyTitle: TextView
        val tvCardButton: TextView
        val tvHeaderExtraTitle: TextView
        val rlTitle: RelativeLayout
        val rvDescription: RecyclerView
        val llCardView: LinearLayout
        val llCardViewTop: LinearLayout
        val vBorder: View

        init {
            llCardView = itemView.findViewById(R.id.llCardView)
            llCardViewTop = itemView.findViewById(R.id.llCardViewTop)
            botListItemTitle = itemView.findViewById(R.id.bot_list_item_title)
            botListItemSubtitle = itemView.findViewById(R.id.bot_list_item_subtitle)
            botListItemImage = itemView.findViewById(R.id.bot_list_item_image)
            tvOnlyTitle = itemView.findViewById(R.id.tvOnlyTitle)
            rlTitle = itemView.findViewById(R.id.rlTitle)
            rvDescription = itemView.findViewById(R.id.rvDescription)
            tvCardButton = itemView.findViewById(R.id.tvCardButton)
            vBorder = itemView.findViewById(R.id.vBorder)
            ivHeaderExtra = itemView.findViewById(R.id.ivheaderExtra)
            tvHeaderExtraTitle = itemView.findViewById(R.id.tvheaderExtraTitle)
        }
    }
}