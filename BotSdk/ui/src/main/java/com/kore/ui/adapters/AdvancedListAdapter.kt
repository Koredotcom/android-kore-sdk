package com.kore.ui.adapters

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kore.common.event.UserActionEvent
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.event.BotChatEvent
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.model.constants.BotResponseConstants.DEFAULT
import com.kore.model.constants.BotResponseConstants.IS_COLLAPSED
import com.kore.model.constants.BotResponseConstants.LARGE
import com.kore.model.constants.BotResponseConstants.LEFT
import com.kore.model.constants.BotResponseConstants.RIGHT
import com.kore.model.constants.BotResponseConstants.SMALL
import com.kore.model.constants.BotResponseConstants.THEME_NAME
import com.kore.ui.R
import com.kore.ui.utils.BitmapUtils

class AdvancedListAdapter(
    val context: Context,
    private val arrAdvanceTemplateModels: List<Map<String, *>>,
    private val isLastItem: Boolean,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : RecyclerView.Adapter<AdvancedListAdapter.AdvanceHolder>() {

    class AdvanceHolder(view: View) : RecyclerView.ViewHolder(view) {
        val botListItemRoot: RelativeLayout = view.findViewById(R.id.bot_list_item_root)
        val rlDropDown: RelativeLayout = view.findViewById(R.id.rlDropDown)
        val rlTitle: RelativeLayout = view.findViewById(R.id.rlTitle)
        val llChildViews: RelativeLayout = view.findViewById(R.id.llChildViews)
        val botListItemImage: ImageView = view.findViewById(R.id.bot_list_item_image)
        val ivAction: ImageView = view.findViewById(R.id.ivAction)
        val ivDropDownAction: ImageView = view.findViewById(R.id.ivDropDownAction)
        val ivDescription: ImageView = view.findViewById(R.id.ivDescription)
        val botListItemTitle: TextView = view.findViewById(R.id.bot_list_item_title)
        val tvAction: TextView = view.findViewById(R.id.tvAction)
        val botListItemSubtitle: TextView = view.findViewById(R.id.bot_list_item_subtitle)
        val rvDefaultButtons: RecyclerView = view.findViewById(R.id.rvDefaultButtons)
        val rvOptionButtons: RecyclerView = view.findViewById(R.id.rvOptionButtons)
        val lvDetails: RecyclerView = view.findViewById(R.id.lvDetails)
        val lvOptions: RecyclerView = view.findViewById(R.id.lvOptions)
        val lvTableList: RecyclerView = view.findViewById(R.id.lvTableList)
        val llOptions: LinearLayout = view.findViewById(R.id.llOptions)
        val llButtonMore: LinearLayout = view.findViewById(R.id.llButtonMore)
        val buttonMore: TextView = view.findViewById(R.id.buttonTV)
        val rlAction: LinearLayout = view.findViewById(R.id.rlAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvanceHolder {
        return AdvanceHolder(LayoutInflater.from(context).inflate(R.layout.advancedlist_cell, parent, false))
    }

    override fun getItemCount(): Int {
        return arrAdvanceTemplateModels.size
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: AdvanceHolder, position: Int) {
        val cardTemplateModel: MutableMap<String, Any> = arrAdvanceTemplateModels[position] as MutableMap<String, Any>
        holder.botListItemTitle.text = cardTemplateModel[BotResponseConstants.KEY_TITLE] as String

        val popUpView = View.inflate(context, R.layout.advancelist_drop_down_popup, null)
        val popupWindow = PopupWindow(popUpView, WRAP_CONTENT, WRAP_CONTENT, true)
        val btnPopUpView = View.inflate(context, R.layout.advancelist_drop_down_popup, null)
        val btnPopUpWindow = PopupWindow(btnPopUpView, WRAP_CONTENT, WRAP_CONTENT, true)

        if (cardTemplateModel[BotResponseConstants.TITLE_STYLES] != null) {
            val titleStyle = cardTemplateModel[BotResponseConstants.TITLE_STYLES] as Map<String, *>
            if (titleStyle[BotResponseConstants.COLOR] != null) holder.botListItemTitle.setTextColor(
                (titleStyle[BotResponseConstants.COLOR] as String).toColorInt()
            )
        }

        val layerDrawable = ResourcesCompat.getDrawable(context.resources, R.drawable.advanced_cell_bg, context.theme) as LayerDrawable?

        if (cardTemplateModel[BotResponseConstants.ELEMENT_STYLES] != null) {
            val elementStyles = cardTemplateModel[BotResponseConstants.ELEMENT_STYLES] as Map<String, *>
            if (elementStyles[BotResponseConstants.BACKGROUND] != null) {
                holder.rlTitle.setBackgroundColor((elementStyles[BotResponseConstants.BACKGROUND] as String).toColorInt())
            }

            if (elementStyles[BotResponseConstants.BORDER_WIDTH] != null) {
                val border: List<String> = (elementStyles[BotResponseConstants.BORDER_WIDTH] as String).split(" ")
                if (border.size > 3) {
                    val border0 = border[0].replace("[^0-9]".toRegex(), "")
                    val border1 = border[1].replace("[^0-9]".toRegex(), "")
                    val border2 = border[2].replace("[^0-9]".toRegex(), "")
                    val border3 = border[3].replace("[^0-9]".toRegex(), "")
                    layerDrawable?.setLayerInset(
                        1,
                        border0.toInt().dpToPx(context),
                        border1.toInt().dpToPx(context),
                        border3.toInt().dpToPx(context),
                        border2.toInt().dpToPx(context)
                    )
                }

                if (elementStyles[BotResponseConstants.BORDER] != null) {
                    val color: List<String> = (elementStyles[BotResponseConstants.BORDER] as String).split(" ")
                    if (layerDrawable != null) {
                        val backgroundColor = layerDrawable.findDrawableByLayerId(R.id.item_bottom_stroke) as GradientDrawable
                        backgroundColor.setColor(color[1].toColorInt())
                        val bagColor = layerDrawable.findDrawableByLayerId(R.id.item_navbar_background) as GradientDrawable
                        if (elementStyles[BotResponseConstants.BORDER_RADIUS] != null) {
                            val radius: String = (elementStyles[BotResponseConstants.BORDER_RADIUS] as String).replace("[^0-9]", "")
                            if (radius.isNotEmpty()) {
                                backgroundColor.cornerRadius = (radius.toInt() * 2).toFloat()
                                bagColor.cornerRadius = (radius.toInt() * 2).toFloat()
                            }
                        }
                    }
                }
            }
        }

        holder.botListItemRoot.background = layerDrawable

        if (cardTemplateModel[BotResponseConstants.DESCRIPTION] != null) {
            holder.botListItemSubtitle.visibility = View.VISIBLE
            holder.botListItemSubtitle.text = cardTemplateModel[BotResponseConstants.DESCRIPTION] as String

            if (cardTemplateModel[BotResponseConstants.DESCRIPTION_STYLES] != null) {
                val desStyles = cardTemplateModel[BotResponseConstants.DESCRIPTION_STYLES] as Map<String, *>
                if (desStyles[BotResponseConstants.COLOR] != null) {
                    holder.botListItemSubtitle.setTextColor((desStyles[BotResponseConstants.COLOR] as String).toColorInt())
                }
            }
        }

        if (cardTemplateModel[BotResponseConstants.ICON] != null) {
            holder.botListItemImage.visibility = View.VISIBLE
            val layoutParams = RelativeLayout.LayoutParams(100, 100)
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
            layoutParams.setMargins(0, 0, 10, 0)

            when (cardTemplateModel[BotResponseConstants.ICON] as String) {
                LARGE -> {
                    layoutParams.height = 180
                    layoutParams.width = 180
                }

                SMALL -> {
                    layoutParams.height = 60
                    layoutParams.width = 60
                }
            }
            holder.botListItemImage.setRoundedCorner(6.dpToPx(holder.itemView.context).toFloat())

            holder.botListItemImage.layoutParams = layoutParams
            try {
                val imageData: String = cardTemplateModel[BotResponseConstants.ICON] as String
                BitmapUtils.loadImage(context, imageData, holder.botListItemImage, R.drawable.ic_image_photo, 10)
            } catch (e: Exception) {
                holder.botListItemImage.visibility = View.GONE
            }
        }

        if (cardTemplateModel[BotResponseConstants.DESCRIPTION_ICON] != null) {
            holder.ivDescription.visibility = View.VISIBLE
            try {
                val imageData = cardTemplateModel[BotResponseConstants.DESCRIPTION_ICON] as String
                BitmapUtils.loadImage(context, imageData, holder.ivDescription, R.drawable.ic_image_photo)
            } catch (e: Exception) {
                holder.ivDescription.visibility = View.GONE
            }

            if (cardTemplateModel[BotResponseConstants.DESCRIPTION_ICON_ALIGNMENT] != null) {
                val params = holder.ivDescription.layoutParams as RelativeLayout.LayoutParams
                val params1 = holder.botListItemTitle.layoutParams as RelativeLayout.LayoutParams
                val params2 = holder.botListItemSubtitle.layoutParams as RelativeLayout.LayoutParams

                when (cardTemplateModel[BotResponseConstants.DESCRIPTION_ICON_ALIGNMENT] as String) {
                    RIGHT -> {
                        params.addRule(RelativeLayout.CENTER_VERTICAL)
                        params.addRule(RelativeLayout.ALIGN_PARENT_END)
                        params1.addRule(RelativeLayout.ALIGN_PARENT_START)
                        params2.addRule(RelativeLayout.BELOW, R.id.bot_list_item_title)
                    }

                    LEFT -> {
                        params.addRule(RelativeLayout.CENTER_VERTICAL)
                        params.addRule(RelativeLayout.ALIGN_PARENT_START)
                        params1.addRule(RelativeLayout.RIGHT_OF, R.id.ivDescription)
                        params2.addRule(RelativeLayout.RIGHT_OF, R.id.ivDescription)
                        params2.addRule(RelativeLayout.BELOW, R.id.bot_list_item_title)
                    }
                }

                holder.botListItemSubtitle.layoutParams = params2
                holder.ivDescription.layoutParams = params
                holder.botListItemTitle.layoutParams = params1
            } else {
                holder.ivDescription.isVisible = false
            }
        }

        if (cardTemplateModel[BotResponseConstants.VIEW] != null) {
            when (cardTemplateModel[BotResponseConstants.VIEW] as String) {
                DEFAULT -> {
                    if (cardTemplateModel[BotResponseConstants.TEXT_INFORMATION] != null) {
                        val textInformation = cardTemplateModel[BotResponseConstants.TEXT_INFORMATION] as List<Map<String, *>>
                        if (textInformation.isNotEmpty()) {
                            holder.lvDetails.visibility = View.VISIBLE
                            holder.lvDetails.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            holder.lvDetails.adapter = AdvanceListDetailsAdapter(context, textInformation)
                        }
                    }

                    if (cardTemplateModel[BotResponseConstants.KEY_BUTTONS] != null) {
                        val buttons = cardTemplateModel[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>
                        val alignment = if (cardTemplateModel.containsKey(BotResponseConstants.BUTTONS_LAYOUT)) {
                            cardTemplateModel[BotResponseConstants.BUTTONS_LAYOUT] as Map<String, *>
                        } else null

                        if (buttons.isNotEmpty()) {
                            var displayLimit = 0
                            holder.rvDefaultButtons.visibility = View.VISIBLE
                            holder.llButtonMore.visibility = View.GONE
                            holder.rvDefaultButtons.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                            if (alignment != null && alignment[BotResponseConstants.DISPLAY_LIMIT] != null) {
                                val display = alignment[BotResponseConstants.DISPLAY_LIMIT] as Map<String, *>
                                displayLimit = (display[BotResponseConstants.COUNT] as String).toInt()
                            }

                            val advanceListButtonAdapter = AdvanceListButtonAdapter(
                                context, when (!alignment.isNullOrEmpty()) {
                                    true -> alignment[BotResponseConstants.BUTTONS_ALIGNMENT] as String
                                    else -> BotResponseConstants.FULL_WIDTH
                                }, buttons, displayLimit, isLastItem, actionEvent
                            )

                            holder.rvDefaultButtons.adapter = advanceListButtonAdapter

                            if (displayLimit != 0) {
                                val tempBtn = mutableListOf<Map<String, *>>()
                                for (i in displayLimit - 1 until buttons.size) {
                                    tempBtn.add(buttons[i])
                                }
                                holder.llButtonMore.visibility = View.VISIBLE
                                btnPopUpView.minimumWidth = 250.dpToPx(context)
                                val recyclerView: RecyclerView = btnPopUpView.findViewById(R.id.rvDropDown)
                                val ivDropDownCLose: ImageView = btnPopUpView.findViewById(R.id.ivDropDownCLose)
                                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                                val advanceButtonAdapter = AdvanceListButtonAdapter(
                                    context, when (!alignment.isNullOrEmpty()) {
                                        true -> alignment[BotResponseConstants.BUTTONS_ALIGNMENT] as String
                                        else -> BotResponseConstants.FULL_WIDTH
                                    }, tempBtn, tempBtn.size, isLastItem, actionEvent
                                )
                                recyclerView.adapter = advanceButtonAdapter
                                ivDropDownCLose.setOnClickListener { btnPopUpWindow.dismiss() }
                            }
                            holder.llButtonMore.setOnClickListener { btnPopUpWindow.showAsDropDown(holder.llButtonMore, -15, -350) }

                            val sharedPrefs = PreferenceRepositoryImpl()
                            val txtColor = sharedPrefs.getStringValue(holder.itemView.context, THEME_NAME, BotResponseConstants.BUBBLE_RIGHT_BG_COLOR, "#3F51B5")
                            holder.buttonMore.setTextColor(txtColor.toColorInt())
                        }
                    }
                }

                BotResponseConstants.OPTIONS -> {
                    var advanceOptionsAdapter: AdvanceOptionsAdapter? = null
                    if (cardTemplateModel[BotResponseConstants.OPTIONS_DATA] != null) {
                        val optionsData = cardTemplateModel[BotResponseConstants.OPTIONS_DATA] as List<Map<String, *>>
                        if (optionsData.isNotEmpty()) {
                            advanceOptionsAdapter = AdvanceOptionsAdapter(context, optionsData, isLastItem)
                            holder.llOptions.visibility = View.VISIBLE
                            holder.lvOptions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            holder.lvOptions.adapter = advanceOptionsAdapter
                        }
                    }

                    if (cardTemplateModel[BotResponseConstants.KEY_BUTTONS] != null) {
                        val buttons = cardTemplateModel[BotResponseConstants.KEY_BUTTONS] as List<Map<String, *>>

                        if (cardTemplateModel[BotResponseConstants.BUTTONS_ALIGNMENT] != null) {
                            val alignment = cardTemplateModel[BotResponseConstants.BUTTONS_ALIGNMENT] as String

                            if (alignment.isNotEmpty()) {
                                val params = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                                params.gravity = Gravity.START
                                if (alignment == RIGHT) params.gravity = Gravity.END
                                holder.rvOptionButtons.layoutParams = params
                            }
                        }

                        holder.rvOptionButtons.visibility = View.VISIBLE
                        holder.rvOptionButtons.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        holder.rvOptionButtons.adapter =
                            AdvanceListButtonAdapter(context, DEFAULT, buttons, buttons.size, isLastItem, actionEvent).apply {
                                setAdvancedOptionsAdapter(advanceOptionsAdapter)
                            }
                    }
                }

                BotResponseConstants.TABLE -> {
                    if (cardTemplateModel[BotResponseConstants.TABLE_LIST_DATA] != null) {
                        val table = cardTemplateModel[BotResponseConstants.TABLE_LIST_DATA] as List<Map<String, *>>
                        if (table.isNotEmpty()) {
                            holder.lvTableList.visibility = View.VISIBLE
                            holder.lvTableList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                            holder.lvTableList.adapter = AdvanceTableListOuterAdapter(context, table)
                        }
                    }
                }
            }
        }

        if (cardTemplateModel[BotResponseConstants.HEADER_OPTIONS] != null) {
            val headerOptions = cardTemplateModel[BotResponseConstants.HEADER_OPTIONS] as List<Map<String, *>>
            if (headerOptions.isNotEmpty()) {
                holder.rlAction.visibility = View.VISIBLE
                for (headerOptionsModel in headerOptions) {
                    when (if (headerOptionsModel[BotResponseConstants.CONTENT_TYPE] != null) headerOptionsModel[BotResponseConstants.CONTENT_TYPE] as String else headerOptionsModel[BotResponseConstants.TYPE] as String) {
                        BotResponseConstants.ICON -> {
                            holder.ivAction.visibility = View.VISIBLE
                            try {
                                val imageData = headerOptionsModel[BotResponseConstants.ICON] as String
                                BitmapUtils.loadImage(context, imageData, holder.ivAction, R.drawable.ic_image_photo)
                            } catch (ex: java.lang.Exception) {
                                holder.ivAction.visibility = View.GONE
                            }
                        }

                        BotResponseConstants.TEMPLATE_TYPE_BUTTON -> {
                            holder.tvAction.visibility = View.VISIBLE
                            holder.tvAction.text = headerOptionsModel[BotResponseConstants.KEY_TITLE] as String
                            holder.tvAction.movementMethod = null
                            if (headerOptionsModel[BotResponseConstants.BUTTON_STYLES] != null) {
                                val buttonStyle = headerOptionsModel[BotResponseConstants.BUTTON_STYLES] as Map<String, *>
                                val gradientDrawable = holder.tvAction.background as GradientDrawable
                                gradientDrawable.cornerRadius = 3.dpToPx(context).toFloat()
                                if (buttonStyle[BotResponseConstants.BORDER_COLOR] != null) {
                                    val borderColor = buttonStyle[BotResponseConstants.BORDER_COLOR] as String
                                    if (!borderColor.contains("#")) {
                                        gradientDrawable.setStroke(1.dpToPx(context), "#$borderColor".toColorInt())
                                    } else {
                                        gradientDrawable.setStroke(1.dpToPx(context), borderColor.toColorInt())
                                    }
                                } else if (buttonStyle[BotResponseConstants.BORDER] != null) {
                                    val border: List<String> = (buttonStyle[BotResponseConstants.BORDER] as String).split("#")
                                    if (border.isNotEmpty()) {
                                        gradientDrawable.setStroke(1.dpToPx(context), ("#" + border[1]).toColorInt())
                                    }
                                } else gradientDrawable.setStroke(1.dpToPx(context), "#224741fa".toColorInt())
                                if (buttonStyle[BotResponseConstants.BACKGROUND] != null) {
                                    val background = buttonStyle[BotResponseConstants.BACKGROUND] as String
                                    if (!background.contains("#")) gradientDrawable.setColor("#$background".toColorInt())
                                    else gradientDrawable.setColor(background.toColorInt())
                                } else {
                                    gradientDrawable.setColor("#224741fa".toColorInt())
                                }

                                if (buttonStyle[BotResponseConstants.COLOR] != null) {
                                    holder.tvAction.setTextColor((buttonStyle[BotResponseConstants.COLOR] as String).toColorInt())
                                }
                            }
                            holder.tvAction.setTypeface(holder.tvAction.typeface, Typeface.BOLD)
                            holder.tvAction.setOnClickListener {
                                val listElementButtonPayload = if (headerOptionsModel[BotResponseConstants.PAYLOAD] != null) {
                                    headerOptionsModel[BotResponseConstants.PAYLOAD] as String
                                } else ""

                                val listElementButtonTitle = if (headerOptionsModel[BotResponseConstants.KEY_TITLE] != null) {
                                    headerOptionsModel[BotResponseConstants.KEY_TITLE] as String
                                } else ""

                                try {
                                    when (headerOptionsModel[BotResponseConstants.TYPE]) {
                                        BotResponseConstants.USER_INTENT, BotResponseConstants.URL, BotResponseConstants.WEB_URL -> {
                                            if (cardTemplateModel[BotResponseConstants.URL] != null)
                                                actionEvent(BotChatEvent.UrlClick(headerOptionsModel[BotResponseConstants.URL] as String))
                                        }

                                        else -> {
                                            if (listElementButtonTitle.isNotEmpty() && listElementButtonPayload.isNotEmpty()) {
                                                if (!isLastItem) return@setOnClickListener
                                                actionEvent(BotChatEvent.SendMessage(listElementButtonTitle, listElementButtonPayload))
                                            } else if (listElementButtonPayload.isNotEmpty()) {
                                                if (!isLastItem) return@setOnClickListener
                                                actionEvent(BotChatEvent.SendMessage(listElementButtonPayload, listElementButtonPayload))
                                            } else {
                                                cardTemplateModel[IS_COLLAPSED] = !(cardTemplateModel[IS_COLLAPSED] as Boolean)
                                                holder.llChildViews.isVisible = cardTemplateModel[IS_COLLAPSED] as Boolean
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        BotResponseConstants.DROP_DOWN -> {
                            if (headerOptionsModel[BotResponseConstants.DROP_DOWN_OPTIONS] != null) {
                                val options = headerOptionsModel[BotResponseConstants.DROP_DOWN_OPTIONS] as List<Map<String, *>>
                                if (options.isNotEmpty()) {
                                    holder.rlDropDown.visibility = View.VISIBLE
                                    popUpView.minimumWidth = 150.dpToPx(context)
                                    val recyclerView = popUpView.findViewById<RecyclerView>(R.id.rvDropDown)
                                    val ivDropDownCLose = popUpView.findViewById<ImageView>(R.id.ivDropDownCLose)
                                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                                    val advanceListButtonAdapter =
                                        AdvanceListButtonAdapter(
                                            context,
                                            DEFAULT,
                                            options,
                                            options.size,
                                            isLastItem,
                                            actionEvent
                                        )
                                    recyclerView.adapter = advanceListButtonAdapter
                                    ivDropDownCLose.setOnClickListener { popupWindow.dismiss() }
                                    holder.ivDropDownAction.setOnClickListener {
                                        popupWindow.showAsDropDown(holder.ivDropDownAction, -400, 0)
                                    }
                                }
                            }
                        }

                        else -> {
                            if (headerOptionsModel[BotResponseConstants.VALUE] != null) {
                                holder.tvAction.visibility = View.VISIBLE
                                holder.tvAction.text = headerOptionsModel[BotResponseConstants.VALUE] as String
                                holder.tvAction.movementMethod = null
                                val gradientDrawable =
                                    holder.botListItemRoot.findViewById<View>(R.id.tvAction).background as GradientDrawable
                                gradientDrawable.setStroke(0, null)
                                if (headerOptionsModel[BotResponseConstants.STYLES] != null) {
                                    val style = headerOptionsModel[BotResponseConstants.STYLES] as Map<String, *>

                                    if (style[BotResponseConstants.COLOR] != null) holder.tvAction.setTextColor(
                                        (style[BotResponseConstants.COLOR] as String).toColorInt()
                                    )

                                    if (style[BotResponseConstants.BORDER_COLOR] != null) {
                                        val borderColor = style[BotResponseConstants.BORDER_COLOR] as String
                                        if (!borderColor.contains("#")) {
                                            gradientDrawable.setStroke(1.dpToPx(context), "#$borderColor".toColorInt())
                                        } else {
                                            gradientDrawable.setStroke(1.dpToPx(context), borderColor.toColorInt())
                                        }
                                    } else if (style[BotResponseConstants.BORDER] != null) {
                                        val border: List<String> = (style[BotResponseConstants.BORDER] as String).split("#")
                                        if (border.isNotEmpty()) {
                                            gradientDrawable.setStroke(1.dpToPx(context), ("#" + border[1]).toColorInt())
                                        }
                                    } else {
                                        gradientDrawable.setStroke(1.dpToPx(context), "#ffffff".toColorInt())
                                    }

                                    if (style[BotResponseConstants.BACKGROUND] != null) {
                                        val background = style[BotResponseConstants.BACKGROUND] as String
                                        if (!background.contains("#")) {
                                            gradientDrawable.setColor("#$background".toColorInt())
                                        } else {
                                            gradientDrawable.setColor(background.toColorInt())
                                        }
                                    } else {
                                        gradientDrawable.setColor("#ffffff".toColorInt())
                                    }
                                }
                                holder.tvAction.setTypeface(holder.tvAction.typeface, Typeface.BOLD)
                            }
                        }
                    }
                }

                holder.ivAction.setOnClickListener {
                    cardTemplateModel[IS_COLLAPSED] = !(cardTemplateModel[IS_COLLAPSED] as Boolean)
                    holder.llChildViews.isVisible = cardTemplateModel[IS_COLLAPSED] as Boolean
                }
            }
        }

        holder.llChildViews.visibility = View.GONE

        if (cardTemplateModel[IS_COLLAPSED] != null && cardTemplateModel[IS_COLLAPSED] as Boolean) holder.llChildViews.isVisible = true

        holder.botListItemRoot.setOnClickListener {

            val listElementButtonPayload = if (cardTemplateModel[BotResponseConstants.PAYLOAD] != null) {
                cardTemplateModel[BotResponseConstants.PAYLOAD] as String
            } else ""

            val listElementButtonTitle = if (cardTemplateModel[BotResponseConstants.KEY_TITLE] != null) {
                cardTemplateModel[BotResponseConstants.KEY_TITLE] as String
            } else ""

            try {
                when (cardTemplateModel[BotResponseConstants.TYPE]) {
                    BotResponseConstants.USER_INTENT, BotResponseConstants.URL, BotResponseConstants.WEB_URL -> {
                        if (cardTemplateModel[BotResponseConstants.URL] != null) {
                            actionEvent(BotChatEvent.UrlClick(cardTemplateModel[BotResponseConstants.URL] as String))
                        }
                    }

                    else -> {
                        if (listElementButtonPayload.isNotEmpty() && listElementButtonTitle.isNotEmpty()) {
                            if (!isLastItem) return@setOnClickListener
                            actionEvent(BotChatEvent.SendMessage(listElementButtonTitle, listElementButtonPayload))
                        } else if (listElementButtonPayload.isNotEmpty()) {
                            if (!isLastItem) return@setOnClickListener
                            actionEvent(BotChatEvent.SendMessage(listElementButtonPayload, listElementButtonPayload))
                        } else {
                            cardTemplateModel[IS_COLLAPSED] = !(cardTemplateModel[IS_COLLAPSED] as Boolean)
                            holder.llChildViews.isVisible = cardTemplateModel[IS_COLLAPSED] as Boolean
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}