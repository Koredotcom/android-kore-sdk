package kore.botssdk.view.row.chatbot.listwidget

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kore.botssdk.R
import kore.botssdk.activity.GenericWebViewActivity
import kore.botssdk.adapter.ListWidgetAdapter
import kore.botssdk.adapter.ListWidgetButtonAdapter
import kore.botssdk.databinding.ListwidgetViewBinding
import kore.botssdk.event.KoreEventCenter
import kore.botssdk.events.EntityEditEvent
import kore.botssdk.extensions.applyStyles
import kore.botssdk.models.ViewStyles
import kore.botssdk.models.Widget
import kore.botssdk.models.WidgetListElementModel
import kore.botssdk.utils.Constants
import kore.botssdk.view.row.SimpleListAdapter
import kore.botssdk.view.row.SimpleListRow
import kore.botssdk.view.row.chatbot.ChatBotRowType
import kore.botssdk.view.row.chatbot.listwidget.detail.BotListWidgetDetailRow
import kore.botssdk.view.viewUtils.RoundedCornersTransform

class BotListWidgetRow(
    private val id: String,
    private val model: WidgetListElementModel,
    private val btnActiveTextColor: String,
    private val trigger: String,
    private val skillName: String,
) : SimpleListRow {

    override val type: SimpleListRow.SimpleListRowType = ChatBotRowType.ListWidget

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListWidgetRow) return false
        return otherRow.id == id && otherRow.model == model
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is BotListWidgetRow) return false
        return otherRow.btnActiveTextColor == btnActiveTextColor
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        (binding as ListwidgetViewBinding).apply {
            val context = root.context
            applyStyles(stockAvailability, model.stockAvailabilityStyles, R.id.bg_stock_availability)
            applyStyles(topRated, model.topRatedStyles, R.id.bg_top_rated)

            updateHeaderViews(model.stockAvailability, stockAvailability)
            updateHeaderViews(model.topRated, topRated)

            txtTitle.isVisible = !model.title.isNullOrEmpty()
            txtTitle.text = model.title.trim()
            txtTitle.setTextColor(Color.parseColor(btnActiveTextColor))
            txtSubtitle.isVisible = !model.subtitle.isNullOrEmpty()
            txtSubtitle.text = model.subtitle.trim()
            txtSubtitle.setTextColor(Color.parseColor(btnActiveTextColor))

            txtDescription.isVisible = !model.description.isNullOrEmpty()
            txtDescription.text = model.description.trim()
            txtDescription.setTextColor(Color.parseColor(btnActiveTextColor))

            val appendUtterance = Constants.SKILL_SELECTION.equals(Constants.SKILL_HOME, ignoreCase = true)
                    || Constants.SKILL_SELECTION.isEmpty()
                    || skillName.isNotEmpty()
                    && !skillName.equals(Constants.SKILL_SELECTION, ignoreCase = true)

            imgUpDown.setOnClickListener {
                val expanded = buttonLayout.isExpanded
                imgUpDown.setImageResource(if (!expanded) R.drawable.ic_arrow_drop_up_24px else R.drawable.ic_arrow_drop_down_24px)
                buttonLayout.isExpanded = !expanded
            }
            if (model.image != null &&
                !model.image.image_src.isNullOrEmpty() &&
                Patterns.WEB_URL.matcher(model.image.image_src).matches()
            ) {
                val url = model.image.image_src.trim().replace("http://", "https://")
                Picasso.get().load(url).transform(RoundedCornersTransform()).into(imageIcon)
            } else {
                imageIcon.visibility = View.GONE
            }
            if (model.value != null && model.value.type != null) {
                iconImageLoad.isVisible = model.value.type.equals("image")
                iconImage.isVisible = model.value.type.equals("menu")
                tvText.isVisible = model.value.type.equals("text")
                tvUrl.isVisible = model.value.type.equals("url")
                tvValuesLayout.isVisible = model.value.type.equals("button")
                when (model.value.type) {
                    "button" -> {
                        tvButton.setOnClickListener {
                            buttonAction(model.value.button, appendUtterance, context)
                        }
                        var btnTitle: String? = ""
                        btnTitle = model.value.button?.title?:model.value.text
                        if (!btnTitle.isNullOrEmpty()) tvButton.text = btnTitle else tvValuesLayout.isVisible = false
                    }

                    "menu" -> {
                        iconImage.bringToFront()
                        iconImage.setOnClickListener {
                            if (model.value != null && !model.value.menu.isNullOrEmpty()) {
                                //icon_down.setVisibility(VISIBLE);
//                                val bottomSheetDialog = WidgetActionSheetFragment()
//                                bottomSheetDialog.setisFromFullView(false)
//                                bottomSheetDialog.setSkillName(skillName, trigger)
//                                bottomSheetDialog.setData(model, true)
//                                bottomSheetDialog.setVerticalListViewActionHelper(verticalListViewActionHelper)
//                                bottomSheetDialog.show((context as FragmentActivity).supportFragmentManager, "add_tags")
                            }
                        }
                    }

                    "text" -> {
                        tvText.text = model.value.text
                    }

                    "url" -> {
                        val content = SpannableString(if (model.value.url.title != null) model.value.url.title else model.value.url.link)
                        content.setSpan(UnderlineSpan(), 0, content.length, 0)
                        tvUrl.text = content
                        tvUrl.setOnClickListener {
                            if (model.value.url.link != null) {
                                val intent = Intent(context, GenericWebViewActivity::class.java)
                                intent.putExtra(GenericWebViewActivity.EXTRA_URL, model.value.url.link)
                                intent.putExtra(GenericWebViewActivity.EXTRA_HEADER, context.getString(R.string.app_name))
                                context.startActivity(intent)
                            }
                        }
                    }

                    "image" -> {
                        if (model.value.image != null && !model.value.image.image_src.isNullOrEmpty()) {
                            Picasso.get().load(model.value.image.image_src).into(iconImageLoad)
                            iconImageLoad.setOnClickListener {
                                defaultAction(
                                    model.value.image.utterance ?: model.value.image.payload ?: "",
                                    appendUtterance
                                )
                            }
                        }
                    }
                }
            }
            imgUpDown.isVisible = !model.buttons.isNullOrEmpty()
            if (!model.buttons.isNullOrEmpty()) {
                buttonsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                val buttonRecyclerAdapter = ListWidgetButtonAdapter(context, model.buttons, trigger)
                buttonRecyclerAdapter.skillName = skillName
//                buttonRecyclerAdapter.setIsFromFullView(isFullView)
//                buttonRecyclerAdapter.setComposeFooterInterface(composeFooterInterface)
//                buttonRecyclerAdapter.setInvokeGenericWebViewInterface(invokeGenericWebViewInterface)
//                buttonRecyclerAdapter.setBottomSheet(bottomSheetDialog)
                buttonsList.adapter = buttonRecyclerAdapter
            }

            alDetails.isVisible = !model.details.isNullOrEmpty()
            if (!model.details.isNullOrEmpty()) {
                alDetails.layoutManager = LinearLayoutManager(root.context)
                alDetails.adapter = SimpleListAdapter(BotListWidgetRowType.values().asList()).apply {
                    submitList(createDetailsRows())
                }
            }

            innerlayout.setOnClickListener {
                if (model.default_action != null && model.default_action.type != null) {
                    when (model.default_action.type) {
                        "url" -> {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(model.default_action.url))
                            try {
                                context.startActivity(browserIntent)
                            } catch (ex: ActivityNotFoundException) {
                                ex.printStackTrace()
                            }
                        }

                        "postback" -> defaultAction(model.default_action.payload, appendUtterance)
                    }
                }
            }
        }
    }

    private fun applyStyles(textView: TextView, style: ViewStyles?, drawableId: Int) {
        textView.isVisible = style != null
        if (style != null) {
            textView.applyStyles(style)
            if (textView.background is LayerDrawable) {
                (textView.background as LayerDrawable).applyStyles(style, drawableId)
            }
        }
    }

    private fun defaultAction(utterance: String?, appendUtterance: Boolean) {
        val event = EntityEditEvent()
        var msg = StringBuffer("")
        val hashMap = HashMap<String, Any>()
        hashMap["refresh"] = java.lang.Boolean.TRUE
        if (appendUtterance) msg = msg.append(trigger).append(" ")
        msg.append(utterance)
        event.message = msg.toString()
        event.payLoad = Gson().toJson(hashMap)
        event.isScrollUpNeeded = true
        KoreEventCenter.post(event)
    }

    private fun updateHeaderViews(text: String?, view: TextView) {
        view.isVisible = !text.isNullOrEmpty()
        view.text = text
    }

    private fun buttonAction(button: Widget.Button?, appendUtterance: Boolean, context: Context) {
        var utterance: String? = null
        if (button != null) {
            utterance = button.utterance
        }
        if (utterance == null) return
        if (utterance.startsWith("tel:") || utterance.startsWith("mailto:")) {
            if (utterance.startsWith("tel:")) {
                ListWidgetAdapter.launchDialer(context, utterance)
            } else if (utterance.startsWith("mailto:")) {
                ListWidgetAdapter.showEmailIntent(context as Activity?, utterance.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1])
            }
            return
        }
        val event = EntityEditEvent()
        var msg = StringBuffer("")
        val hashMap = java.util.HashMap<String, Any>()
        hashMap["refresh"] = java.lang.Boolean.TRUE
        if (appendUtterance) msg = msg.append(trigger).append(" ")
        msg.append(utterance)
        event.message = msg.toString()
        event.payLoad = Gson().toJson(hashMap)
        event.isScrollUpNeeded = true
        KoreEventCenter.post(event)
    }

    private fun createDetailsRows(): List<BotListWidgetDetailRow> {
        var rows = emptyList<BotListWidgetDetailRow>()
        rows = rows + model.details.map { BotListWidgetDetailRow(id, it, btnActiveTextColor) }

        return rows
    }
}