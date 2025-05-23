package com.kore.ui.row.botchat

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.kore.data.repository.preference.PreferenceRepositoryImpl
import com.kore.extensions.dpToPx
import com.kore.extensions.setRoundedCorner
import com.kore.model.constants.BotResponseConstants
import com.kore.ui.R
import com.kore.ui.adapters.TableResponsiveAdapter
import com.kore.ui.adapters.TableTemplateAdapter
import com.kore.ui.adapters.TableTemplateHeaderAdapter
import com.kore.ui.databinding.TableResponsiveLayoutBinding
import com.kore.ui.row.SimpleListRow

class TableResponsiveRow(
    private val id: String,
    private val iconUrl: String?,
    private val payload: HashMap<String, Any>,
) : SimpleListRow() {
    override val type: SimpleListRowType = BotChatRowType.getRowType(BotChatRowType.ROW_TABLE_RESPONSIVE_PROVIDER)

    override fun areItemsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableResponsiveRow) return false
        return otherRow.id == id
    }

    override fun areContentsTheSame(otherRow: SimpleListRow): Boolean {
        if (otherRow !is TableResponsiveRow) return false
        return otherRow.payload == payload
    }

    override fun <Binding : ViewBinding> bind(binding: Binding) {
        showOrHideIcon(binding, binding.root.context, iconUrl, isShow = false, isTemplate = true)
        val childBinding = TableResponsiveLayoutBinding.bind((binding.root as ViewGroup).getChildAt(1))
        childBinding.apply {
            val columns = payload[BotResponseConstants.COLUMNS] as List<List<String>>
            val rows = payload[BotResponseConstants.KEY_ELEMENTS] as List<Map<String, *>>
            val sharedPrefs = PreferenceRepositoryImpl()
            val bgColor = sharedPrefs.getStringValue(
                root.context,
                BotResponseConstants.THEME_NAME,
                BotResponseConstants.BUBBLE_LEFT_BG_COLOR,
                "#efeffc"
            ).toColorInt()
            rvTableView.setBackgroundColor(bgColor)
            rvTableView.setRoundedCorner(4.dpToPx(root.context).toFloat())

            tvShowMore.setOnClickListener { showTableViewDialog(root.context, columns, rows) }
            rvTableView.layoutManager = LinearLayoutManager(root.context, LinearLayoutManager.VERTICAL, false)
            rvTableView.adapter = TableResponsiveAdapter(root.context, rows, columns)
        }
    }

    private fun showTableViewDialog(context: Context, cols: List<List<String>>, values: List<Map<String, *>>) {
        val dialog = Dialog(context, R.style.MyTableDialogTheme)
        dialog.setContentView(R.layout.table_dialog_view)
        val rlRoot: RelativeLayout = dialog.findViewById(R.id.rlRoot)
        val llContent: LinearLayout = dialog.findViewById(R.id.llContent)
        val rvTableView: RecyclerView = dialog.findViewById(R.id.rvTableView)
        val rvTableViewHeader: RecyclerView = dialog.findViewById(R.id.rvTableViewHeader)
        val ivCancel: ImageView = dialog.findViewById(R.id.ivCancel)

        ivCancel.setOnClickListener { dialog.dismiss() }
        val sharedPrefs = PreferenceRepositoryImpl()
        val bgColor = sharedPrefs.getStringValue(
            rvTableView.context,
            BotResponseConstants.THEME_NAME,
            BotResponseConstants.BUBBLE_LEFT_BG_COLOR,
            "#efeffc"
        ).toColorInt()
        llContent.setBackgroundColor(bgColor)
        rlRoot.setRoundedCorner(4.dpToPx(context).toFloat())
        llContent.setRoundedCorner(4.dpToPx(context).toFloat())

        rvTableView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvTableView.adapter = TableTemplateAdapter(context, values, cols)
        rvTableViewHeader.layoutManager = GridLayoutManager(context, cols.size)
        rvTableViewHeader.adapter = TableTemplateHeaderAdapter(context, cols)
        dialog.show()
    }
}