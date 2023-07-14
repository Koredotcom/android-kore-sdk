package kore.botssdk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.R
import kore.botssdk.extensions.clearItemDecorations
import kore.botssdk.itemdecorators.StoreTimingItemDecoration
import kore.botssdk.listener.ComposeFooterInterface
import kore.botssdk.listener.InvokeGenericWebViewInterface
import kore.botssdk.models.NearByStockAvailableStoreModel

class NearByStockAvailableStoresAdapter(
    private val context: Context,
    private var nearByStockAvailableStoreModels: List<NearByStockAvailableStoreModel>
) : RecyclerView.Adapter<NearByStockAvailableStoresAdapter.ViewHolder>() {

    private var composeFooterInterface: ComposeFooterInterface? = null

    private var invokeGenericWebViewInterface: InvokeGenericWebViewInterface? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.near_by_stock_available_store_view, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = nearByStockAvailableStoreModels[position]
        holder.nextClosestInStock.isVisible = !model.nextClosestInStock.isNullOrEmpty()
        holder.itemsLeft.isVisible = model.itemsLeft != null
        holder.nextClosestInStock.text = model.nextClosestInStock
        holder.location.text = model.location
        holder.storeAvailability.text = model.openStatus
        holder.itemsLeft.text = context.getString(R.string.items_left_only, model.itemsLeft)
        holder.distance.text = model.distanceToStore
        holder.storeAddress.text = model.storeAddress
        holder.actionButton.text = model.action
        holder.storeTimings.layoutManager = LinearLayoutManager(context)
        model.storeTimings?.let {
            holder.storeTimings.adapter = StoreTimingsAdapter(it)
            holder.storeTimings.addItemDecoration(StoreTimingItemDecoration(context))
            holder.storeTimings.clearItemDecorations()
        }
        holder.actionButton.setOnClickListener {
            composeFooterInterface?.onSendClick(model.action, false)
        }
        holder.storeInfo.setOnClickListener {
            holder.layoutStoreInfo.isVisible = !holder.layoutStoreInfo.isVisible
            it.isSelected = !it.isSelected
        }
    }

    override fun getItemCount(): Int {
        return nearByStockAvailableStoreModels.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: View
        val nextClosestInStock: TextView
        val location: TextView
        val storeAvailability: TextView
        val itemsLeft: TextView
        val distance: TextView
        val storeAddress: TextView
        val actionButton: TextView
        val storeTimings: RecyclerView
        val storeInfo: ImageView
        val layoutStoreInfo: LinearLayoutCompat

        init {
            root = itemView
            nextClosestInStock = itemView.findViewById(R.id.next_closest_in_stock)
            location = itemView.findViewById(R.id.location)
            storeAvailability = itemView.findViewById(R.id.store_availability)
            itemsLeft = itemView.findViewById(R.id.items_left)
            distance = itemView.findViewById(R.id.distance)
            storeAddress = itemView.findViewById(R.id.store_address)
            actionButton = itemView.findViewById(R.id.action_button)
            storeTimings = itemView.findViewById(R.id.store_timings)
            storeInfo = itemView.findViewById(R.id.store_info)
            layoutStoreInfo = itemView.findViewById(R.id.layout_store_info)
        }
    }

    fun setComposeFooterInterface(composeFooterInterface: ComposeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface
    }

    fun setInvokeGenericWebViewInterface(invokeGenericWebViewInterface: InvokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface
    }

    fun updateList(models: List<NearByStockAvailableStoreModel>) {
        nearByStockAvailableStoreModels = models
        notifyDataSetChanged()
    }
}