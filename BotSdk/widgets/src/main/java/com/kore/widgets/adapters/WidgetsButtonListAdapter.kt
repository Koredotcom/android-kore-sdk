package com.kore.widgets.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kore.widgets.R
import com.kore.widgets.event.BaseActionEvent
import com.kore.widgets.event.WidgetActionEvent
import com.kore.widgets.model.ButtonModel
import java.util.Locale

class WidgetsButtonListAdapter(
    private val context: Context,
    private val buttons: List<ButtonModel>?,
    private val trigger: String?,
    private val actionEvent: (event: BaseActionEvent) -> Unit
) : RecyclerView.Adapter<WidgetsButtonListAdapter.ButtonViewHolder?>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var skillName: String? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ButtonViewHolder {
        return ButtonViewHolder(inflater.inflate(R.layout.button_list_item, viewGroup, false))
    }

    override fun getItemCount(): Int = buttons?.size ?: 0

    override fun onBindViewHolder(holder: ButtonViewHolder, i: Int) {
        val btn = buttons!![i]
        holder.tv.text = btn.title
        try {
            holder.tv.setTextColor(Color.parseColor(btn.theme))
        } catch (e: Exception) {
            holder.tv.setTextColor(Color.parseColor("#3942f6"))
        }
        holder.tv.setOnClickListener { buttonAction(context, btn, !skillName.isNullOrEmpty()) }
    }

    var isFullView = false

    fun setIsFromFullView(isFullView: Boolean) {
        this.isFullView = isFullView
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv: TextView

        init {
            tv = itemView.findViewById(R.id.buttonTV)
        }
    }

    private fun buttonAction(context: Context, btn: ButtonModel?, appendUtterance: Boolean) {
        if (btn != null) {
            if (btn.type != null && btn.type == "url") {
                var url = btn.url
                if (!url.isNullOrEmpty()) {
                    if (!url.startsWith("http")) {
                        url = "http://" + url.lowercase(Locale.getDefault())
                    }
                    actionEvent(WidgetActionEvent.UrlClick(url, ""))
//                    if (NetworkUtility.isNetworkConnectionAvailable(mContext)) {
//                        val intent = Intent(context, GenericWebViewActivity::class.java)
//                        intent.putExtra("url", url)
//                        intent.putExtra("header", context.resources.getString(R.string.app_name))
//                        mContext.startActivity(intent)
//                    } else {
//                        Toast.makeText(mContext, "Check your internet connection and please try again", Toast.LENGTH_LONG).show()
//                    }
                }
            } else {
                var utterance: String? = null
                if (!btn.payload.isNullOrEmpty()) {
                    utterance = btn.payload
                }
                if (btn.utterance?.isNotEmpty() == true && utterance == null) {
                    utterance = btn.utterance
                }

                var msg = StringBuffer()
                val hashMap = HashMap<String, Any>()
                hashMap["refresh"] = java.lang.Boolean.TRUE
                if (appendUtterance && trigger != null) msg = msg.append(trigger).append(" ")
                msg.append(utterance)
                actionEvent(WidgetActionEvent.SendMessageFromPanel(msg.toString(), true, Gson().toJson(hashMap).toString()))
                try {
                    if (isFullView) {
                        (context as Activity).finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}