package com.kore.ui.row

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kore.botclient.databinding.BaseRowBinding
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.row.listener.ChatContentStateListener
import androidx.core.graphics.toColorInt

abstract class SimpleListRow {
    companion object {
        val MATERIAL_COLORS = intArrayOf("#4A9AF2".toColorInt(), "#5BC8C4".toColorInt(), "#e74c3c".toColorInt(), "#3498db".toColorInt())
        val HOLO_BLUE = Color.rgb(51, 181, 229)
    }

    abstract val type: SimpleListRowType

    protected var contentStateListener: ChatContentStateListener? = null

    protected var bottomSheetDialog: BottomSheetDialog? = null

    fun setTemplateBottomSheetDialog(bottomSheetDialog: BottomSheetDialog?){
        this.bottomSheetDialog = bottomSheetDialog
    }

    abstract fun areItemsTheSame(otherRow: SimpleListRow): Boolean

    // If any content change for the second time then this fun should return true(by mentioning conditions) otherwise return false
    abstract fun areContentsTheSame(otherRow: SimpleListRow): Boolean

    // This method need to be overridden if any ui changes required based on user interaction.
    open fun getChangePayload(otherRow: SimpleListRow): Any? = null

    abstract fun <Binding : ViewBinding> bind(binding: Binding)

    // This method need to be implement if getChangePayload(otherRow: SimpleListRow): Any is true. Otherwise not required.
    open fun <Binding : ViewBinding> bindWithPayload(binding: Binding, payload: List<Any>) {}

    fun setStateListener(listener: ChatContentStateListener) {
        contentStateListener = listener
    }

    fun showOrHideIcon(binding: ViewBinding, context: Context, url: String?, isShow: Boolean, isTemplate: Boolean) {
        (binding as BaseRowBinding).apply {
            botIcon.isVisible = isShow && bottomSheetDialog == null
            if (!isShow || bottomSheetDialog != null) return

            val isTimeStampVisible = PreferenceRepositoryImpl()
                .getSharedPreference(root.context, BotResponseConstants.THEME_NAME)
                .getBoolean(BotResponseConstants.IS_TIME_STAMP_REQUIRED, false)

            if (isTimeStampVisible && !isTemplate) {
                val layoutParams = botIcon.layoutParams as LayoutParams
                layoutParams.topMargin = (25.dpToPx(context))
                layoutParams.marginEnd = (5.dpToPx(context))
                botIcon.layoutParams = layoutParams
            }

            if (!url.isNullOrEmpty()) {
                Glide.with(context).load(url.toString()).error(com.kore.botclient.R.drawable.ic_launcher)
                    .into<DrawableImageViewTarget>(DrawableImageViewTarget(botIcon))
            } else {
                botIcon.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.mipmap.ic_launcher, context.getTheme()))
            }
        }
    }


    class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.bottom = verticalSpaceHeight
        }
    }

    class HorizontalSpaceItemDecoration(private val horizontalSpace: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.right = horizontalSpace
        }
    }


    interface SimpleListRowType {

        val ordinal: Int

        val provider: SimpleListViewHolderProvider<*>
    }
}