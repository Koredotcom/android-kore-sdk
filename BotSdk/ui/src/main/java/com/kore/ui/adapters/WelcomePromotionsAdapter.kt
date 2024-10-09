package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kore.network.api.responsemodels.branding.PromotionsModel
import com.kore.ui.R

class WelcomePromotionsAdapter(val context : Context, val arrPromotions : ArrayList<PromotionsModel>) :
    BaseAdapter()
{
    override fun getCount(): Int {
        return arrPromotions.size
    }

    override fun getItem(position: Int): Any {
        return arrPromotions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.promotions_banner, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        val media = arrPromotions[position].banner

        Glide.with(context)
            .load(media)
            .into(vh.ivPromotions)
            .onLoadFailed(ResourcesCompat.getDrawable(context.resources, R.drawable.ic_launcher_foreground, context.theme))

        return view
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val ivPromotions : ImageView

        init {
            ivPromotions = itemView.findViewById(R.id.ivPromotionsBanner)
        }
    }
}