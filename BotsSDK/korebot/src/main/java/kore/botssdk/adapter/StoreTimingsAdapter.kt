package kore.botssdk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.R
import kore.botssdk.models.StoreTimingsModel

class StoreTimingsAdapter(
    private val botStoreTimings: List<StoreTimingsModel>,
) : RecyclerView.Adapter<StoreTimingsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.store_timings_view, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (dayOfWeek, storeTiming) = botStoreTimings[position]
        holder.dayOfWeek.text = dayOfWeek
        holder.storeTiming.text = storeTiming
    }

    override fun getItemCount(): Int {
        return botStoreTimings.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dayOfWeek: TextView
        var storeTiming: TextView

        init {
            dayOfWeek = itemView.findViewById(R.id.day_of_week)
            storeTiming = itemView.findViewById(R.id.store_timing)
        }
    }
}