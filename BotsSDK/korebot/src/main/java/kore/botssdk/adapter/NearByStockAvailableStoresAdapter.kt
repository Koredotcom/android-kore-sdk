package kore.botssdk.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.R
import kore.botssdk.extensions.clearItemDecorations
import kore.botssdk.extensions.dpToPx
import kore.botssdk.itemdecorators.StoreTimingItemDecoration
import kore.botssdk.listener.ComposeFooterInterface
import kore.botssdk.listener.InvokeGenericWebViewInterface
import kore.botssdk.models.BotResponse
import kore.botssdk.models.NearByStockAvailableStoreModel
import kore.botssdk.net.SDKConfiguration

class NearByStockAvailableStoresAdapter(
    private val context: Context,
    private var nearByStockAvailableStoreModels: List<NearByStockAvailableStoreModel>
) : RecyclerView.Adapter<NearByStockAvailableStoresAdapter.ViewHolder>() {

    private var composeFooterInterface: ComposeFooterInterface? = null

    private var invokeGenericWebViewInterface: InvokeGenericWebViewInterface? = null

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE)

    private var themeName: String? = null

    init {
        themeName = sharedPreferences.getString(BotResponse.APPLY_THEME_NAME, BotResponse.THEME_NAME_1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.near_by_stock_available_store_view, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = nearByStockAvailableStoreModels[position]
        holder.nextClosestInStock.text = model.nextClosestInStock
        holder.storeAvailability.text = model.openStatus
        holder.itemsLeft.text = context.getString(R.string.items_left_only, model.itemsLeft)
        holder.distance.text = model.openStatus
        holder.storeAddress.text = model.openStatus
        holder.actionButton.text = model.openStatus
        model.storeTimings?.let {
            holder.storeTimings.adapter = StoreTimingsAdapter(it)
            holder.storeTimings.addItemDecoration(StoreTimingItemDecoration(context))
            holder.storeTimings.clearItemDecorations()
        }
        holder.actionButton.setOnClickListener {
            composeFooterInterface?.onSendClick(model.action, false)
        }
        val backgroundDrawable = holder.root.background as GradientDrawable
        if (themeName.equals(BotResponse.THEME_NAME_1, ignoreCase = true)) {
            backgroundDrawable.setStroke(
                1.dpToPx(context), Color.parseColor(
                    sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, "#ffffff")
                )
            )
        } else {
            backgroundDrawable.setStroke(
                1.dpToPx(context), Color.parseColor(
                    sharedPreferences.getString(BotResponse.WIDGET_BORDER_COLOR, SDKConfiguration.BubbleColors.rightBubbleUnSelected)
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return nearByStockAvailableStoreModels.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val root: View
        val nextClosestInStock: TextView
        val storeAvailability: TextView
        val itemsLeft: TextView
        val distance: TextView
        val storeAddress: TextView
        val actionButton: TextView
        val storeTimings: RecyclerView

        init {
            root = itemView
            nextClosestInStock = itemView.findViewById(R.id.day_of_week)
            storeAvailability = itemView.findViewById(R.id.store_timing)
            itemsLeft = itemView.findViewById(R.id.items_left)
            distance = itemView.findViewById(R.id.distance)
            storeAddress = itemView.findViewById(R.id.store_address)
            actionButton = itemView.findViewById(R.id.action_button)
            storeTimings = itemView.findViewById(R.id.store_timings)
        }
    }

    fun setComposeFooterInterface(composeFooterInterface: ComposeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface
    }

    fun setInvokeGenericWebViewInterface(invokeGenericWebViewInterface: InvokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface
    }

    fun updateList(models: List<NearByStockAvailableStoreModel>){
        nearByStockAvailableStoreModels = models
        notifyDataSetChanged()
    }
}