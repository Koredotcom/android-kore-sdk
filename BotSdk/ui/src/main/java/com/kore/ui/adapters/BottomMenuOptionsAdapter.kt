package com.kore.ui.adapters

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kore.common.event.UserActionEvent
import com.kore.event.BotChatEvent
import com.kore.model.constants.BotResponseConstants
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonActionModel
import com.kore.ui.R

class BottomMenuOptionsAdapter(private val models: ArrayList<BrandingQuickStartButtonActionModel>) : RecyclerView.Adapter<BottomMenuOptionsAdapter.ViewHolder?>() {
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var sharedPreferences: SharedPreferences? = null
    private var actionEvent: (event: UserActionEvent) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = layoutInflater.inflate(R.layout.bottom_options_item, parent, false)
        sharedPreferences = parent.context.getSharedPreferences(BotResponseConstants.THEME_NAME, Context.MODE_PRIVATE)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val botListModel: BrandingQuickStartButtonActionModel = models[position]
        holder.bottomOptionImage.visibility = View.GONE

        if (!botListModel.icon.isNullOrEmpty()) {
            try {
                holder.bottomOptionImage.visibility = View.VISIBLE
                var imageData: String
                imageData = botListModel.icon!!
                if (imageData.contains(",")) {
                    imageData = imageData.substring(imageData.indexOf(",") + 1)
                    val decodedString = Base64.decode(imageData.toByteArray(), Base64.DEFAULT)
                    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    holder.bottomOptionImage.setImageBitmap(decodedByte)
                }
            } catch (e: Exception) {
                holder.bottomOptionImage.visibility = View.GONE
            }
        }

        holder.bottomOptionName.text = botListModel.title
        holder.botListItemRoot.setOnClickListener {
            bottomSheetDialog?.dismiss()
            models[holder.adapterPosition].title?.let {
                actionEvent(BotChatEvent.SendMessage(it.trim(), models[holder.adapterPosition].value))
            }
        }
    }

    fun setBotListModelArrayList(bottomSheetDialog: BottomSheetDialog?) {
        this.bottomSheetDialog = bottomSheetDialog
    }

    override fun getItemCount(): Int = models.size

    fun setActionEvent(actionEvent: (event: UserActionEvent) -> Unit) {
        this.actionEvent = actionEvent
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bottomOptionImage: ImageView = itemView.findViewById(R.id.bottom_option_image)
        val bottomOptionName: TextView = itemView.findViewById(R.id.bottom_option_name)
        val botListItemRoot: LinearLayout = itemView.findViewById(R.id.bot_list_item_root)
    }
}
