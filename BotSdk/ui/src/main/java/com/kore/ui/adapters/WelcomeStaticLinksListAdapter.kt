package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonButtonsModel
import com.kore.ui.R

class WelcomeStaticLinksListAdapter(val context: Context, private val arrStaticLinks : ArrayList<BrandingQuickStartButtonButtonsModel>) : RecyclerView.Adapter<WelcomeStaticLinksListAdapter.QuickReplyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuickReplyViewHolder {
        val convertView: View = LayoutInflater.from(context).inflate(R.layout.welcome_static_links, parent, false)
        return QuickReplyViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: QuickReplyViewHolder, position: Int) {
        val quickReplyTemplate: BrandingQuickStartButtonButtonsModel = arrStaticLinks[position]
        holder.quickReplyTitle.text = quickReplyTemplate.title
        holder.quickReplyDesc.text = quickReplyTemplate.description
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return arrStaticLinks.size
    }

    class QuickReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val quickReplyRoot: LinearLayout
        val quickReplyTitle: TextView
        val quickReplyDesc: TextView

        init {
            quickReplyTitle = view.findViewById(R.id.link_title)
            quickReplyDesc = view.findViewById(R.id.link_desc)
            quickReplyRoot = view.findViewById(R.id.quick_reply_item_root)
        }
    }

}