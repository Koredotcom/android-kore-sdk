package com.kore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager.widget.PagerAdapter
import com.kore.network.api.responsemodels.branding.BrandingQuickStartButtonButtonsModel
import com.kore.ui.R

class WelcomeStaticLinksAdapter(val context : Context,
                                private val arrStaticLinks: ArrayList<BrandingQuickStartButtonButtonsModel>
): PagerAdapter()
{
        override fun getCount(): Int {
        return this@WelcomeStaticLinksAdapter.arrStaticLinks.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any
    {
        val carouselItemLayout: View = LayoutInflater.from(context).inflate(R.layout.welcome_static_links, container, false)
        val staticViewHolder = StaticViewHolder(carouselItemLayout)
        staticViewHolder.link_title.text = arrStaticLinks[position].title
        staticViewHolder.link_desc.text = arrStaticLinks[position].description
        container.addView(carouselItemLayout)
        return carouselItemLayout
    }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    class StaticViewHolder(itemView: View) : ViewHolder(itemView)
    {
        var link_title : TextView = itemView.findViewById(R.id.link_title)
        var link_desc : TextView = itemView.findViewById(R.id.link_desc)
    }
}