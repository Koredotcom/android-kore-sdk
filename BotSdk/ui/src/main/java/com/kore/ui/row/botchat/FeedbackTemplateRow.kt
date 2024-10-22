package com.kore.ui.row.botchat

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kore.common.event.UserActionEvent
import com.kore.ui.row.SimpleListRow
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants.KEY_TEXT
import com.kore.model.constants.BotResponseConstants.NUMBERS_ARRAYS
import com.kore.model.constants.BotResponseConstants.SELECTED_FEEDBACK
import com.kore.model.constants.BotResponseConstants.THUMBS_UP_DOWN_ARRAYS
import com.kore.model.constants.BotResponseConstants.VIEW
import com.kore.model.constants.BotResponseConstants.VIEW_CSAT
import com.kore.model.constants.BotResponseConstants.VIEW_NPS
import com.kore.model.constants.BotResponseConstants.VIEW_STAR
import com.kore.model.constants.BotResponseConstants.VIEW_THUMBS_UP_DOWN
import com.kore.ui.R
import com.kore.ui.databinding.RowFeedbackTemplateBinding
import com.kore.ui.row.botchat.feedback.adapter.FeedbackRatingScaleAdapter
import com.kore.ui.row.botchat.feedback.adapter.FeedbackThumbsAdapter

class FeedbackTemplateRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
    private val selectedFeedback: Int,
    private val isLastItem: Boolean,
    private val onFeedBackSelected: (id: String, selectedPosition: Int, key: String) -> Unit,
    private val actionEvent: (event: UserActionEvent) -> Unit
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_FEEDBACK_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is FeedbackTemplateRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is FeedbackTemplateRow) return false
        return otherRow.payload == payload && otherRow.selectedFeedback == selectedFeedback && otherRow.isLastItem == isLastItem
    }

    override fun getChangePayload(otherRow: SimpleListRow): Any {
        return true
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = RowFeedbackTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            tvFeedbackTemplateTitle.text = payload[KEY_TEXT].toString()
            val viewType = payload[VIEW]
            emojis.root.isVisible = viewType == VIEW_CSAT
            rbFeedback.isVisible = viewType == VIEW_STAR
            rlViewNPS.isVisible = viewType == VIEW_NPS
            thumbsUpDown.isVisible = viewType == VIEW_THUMBS_UP_DOWN
            commonBind()
        }
    }

    override fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {
        RowFeedbackTemplateBinding.bind((binding.root as ViewGroup).getChildAt(1)).commonBind()
    }

    private fun RowFeedbackTemplateBinding.commonBind() {
        when (payload[VIEW]) {
            VIEW_STAR -> {
                rbFeedback.rating = selectedFeedback.toFloat()
                rbFeedback.setOnRatingBarChangeListener { _, rating, fromUser ->
                    if (fromUser) {
                        val value = rating.toInt()
                        onFeedBackSelected(id, value, SELECTED_FEEDBACK)
                        actionEvent(BotChatEvent.SendMessage(value.toString(), value.toString()))
                    }
                }
                rbFeedback.setIsIndicator(!isLastItem)
            }

            VIEW_NPS -> {
                rvRatingScale.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.HORIZONTAL, false)
                rvRatingScale.adapter = FeedbackRatingScaleAdapter(
                    id,
                    payload[NUMBERS_ARRAYS] as List<HashMap<String, Int>>,
                    selectedFeedback,
                    isLastItem,
                    onFeedBackSelected,
                    actionEvent
                )
            }

            VIEW_CSAT -> {
                loadEmojis(selectedFeedback, this)
                emojis.icon1.setOnClickListener { onEmojiSelected(1) }
                emojis.icon2.setOnClickListener { onEmojiSelected(2) }
                emojis.icon3.setOnClickListener { onEmojiSelected(3) }
                emojis.icon4.setOnClickListener { onEmojiSelected(4) }
                emojis.icon5.setOnClickListener { onEmojiSelected(5) }
            }

            VIEW_THUMBS_UP_DOWN -> {
                if (!payload.containsKey(THUMBS_UP_DOWN_ARRAYS)) return

                val layoutManager = FlexboxLayoutManager(root.context)
                layoutManager.flexDirection = FlexDirection.ROW
                layoutManager.justifyContent = JustifyContent.FLEX_START
                thumbsUpDown.layoutManager = layoutManager
                thumbsUpDown.addItemDecoration(VerticalSpaceItemDecoration(20))

                val arrFeedbackThumbsModels: List<Map<String, Any>>? = payload[THUMBS_UP_DOWN_ARRAYS] as List<Map<String, Any>>?
                if (!arrFeedbackThumbsModels.isNullOrEmpty()) {
                    val adapter = FeedbackThumbsAdapter(id, arrFeedbackThumbsModels, selectedFeedback, isLastItem, onFeedBackSelected, actionEvent)
                    thumbsUpDown.adapter = adapter
                }
            }
        }
    }

    private fun onEmojiSelected(position: Int) {
        if (isLastItem) {
            onFeedBackSelected(id, position, SELECTED_FEEDBACK)
            actionEvent(BotChatEvent.SendMessage(position.toString(), position.toString()))
        }
    }

    private fun loadEmojis(selectedPosition: Int, binding: RowFeedbackTemplateBinding) {
        val manager = Glide.with(binding.root.context)
        manager.load(if (selectedPosition == 1) R.drawable.feedbac_ic_emo_1 else R.drawable.feedback_icon_1)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.emojis.icon1))
        manager.load(if (selectedPosition == 2) R.drawable.feedbac_ic_emo_2 else R.drawable.feedback_icon_2)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.emojis.icon2))
        manager.load(if (selectedPosition == 3) R.drawable.feedbac_ic_emo_3 else R.drawable.feedback_icon_3)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.emojis.icon3))
        manager.load(if (selectedPosition == 4) R.drawable.feedbac_ic_emo_4 else R.drawable.feedback_icon_4)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.emojis.icon4))
        manager.load(if (selectedPosition == 5) R.drawable.feedbac_ic_emo_5 else R.drawable.feedback_icon_5)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
            .into<DrawableImageViewTarget>(DrawableImageViewTarget(binding.emojis.icon5))
    }
}