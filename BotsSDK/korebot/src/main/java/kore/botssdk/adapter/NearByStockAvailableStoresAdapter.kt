package kore.botssdk.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.EntryPointAccessors
import kore.botssdk.R
import kore.botssdk.customviews.RatioFixedImageView
import kore.botssdk.delegate.EmployeeAssistDelegate
import kore.botssdk.delegate.EmployeeAssistDelegateEntryPoint
import kore.botssdk.extensions.clearItemDecorations
import kore.botssdk.itemdecorators.StoreTimingItemDecoration
import kore.botssdk.listener.ComposeFooterInterface
import kore.botssdk.listener.InvokeGenericWebViewInterface
import kore.botssdk.models.NearByStockAvailableStoreModel
import kore.botssdk.view.viewUtils.RoundedCornersTransform

class NearByStockAvailableStoresAdapter(
    private val context: Context,
    private var nearByStockAvailableStoreModels: List<NearByStockAvailableStoreModel>
) : RecyclerView.Adapter<NearByStockAvailableStoresAdapter.ViewHolder>() {

    companion object {
        private var delegate: EmployeeAssistDelegate? = null
        private const val TAG = "NearByStockAvailableStoresAdapter"
        private const val GOOGLE_STATIC_MAP_URL =
            "https://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&key=%s"

        private fun inject(context: Context) {
            val entryPoint = EntryPointAccessors.fromApplication(
                context.applicationContext,
                EmployeeAssistDelegateEntryPoint::class.java
            )
            delegate = entryPoint.getKoreBotDelegate()
        }
    }

    private var composeFooterInterface: ComposeFooterInterface? = null

    private var invokeGenericWebViewInterface: InvokeGenericWebViewInterface? = null

    private var isLastItem = false

    private var roundedCornersTransform: RoundedCornersTransform? = null

//    private val sharedPreferences = context.getSharedPreferences(THEME_NAME, MODE_PRIVATE)

    init {
        roundedCornersTransform = RoundedCornersTransform()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.near_by_stock_available_store_view, parent, false)
        inject(listItem.context)
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
        val requestCreator = Picasso.get()
            .load(String.format(GOOGLE_STATIC_MAP_URL, delegate?.getGoogleMapKey() ?: ""))
            .error(R.drawable.edit_btn_blue_bg)
        roundedCornersTransform?.let { requestCreator.transform(it) }
        requestCreator.into(holder.googleMapImage, object : Callback {
            override fun onSuccess() {
                holder.mapErrorMsg.isVisible = false
            }

            override fun onError(e: Exception?) {
                e?.message?.let { Log.e(TAG, it) }
                holder.mapErrorMsg.isVisible = true
            }
        })
        model.storeTimings?.let {
            holder.storeTimings.adapter = StoreTimingsAdapter(it)
            holder.storeTimings.addItemDecoration(StoreTimingItemDecoration(context))
            holder.storeTimings.clearItemDecorations()
        }
        holder.storeInfo.setOnClickListener {
            holder.layoutStoreInfo.isVisible = !holder.layoutStoreInfo.isVisible
            it.isSelected = !it.isSelected
        }
        holder.actionButton.setOnClickListener {
            composeFooterInterface?.onSendClick(model.id, false)
        }
        if (!isLastItem) {
            holder.actionButton.alpha = 0.3f
            holder.actionButton.setOnClickListener(null)
        } else {
            holder.actionButton.alpha = 1.0f
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
        val googleMapImage: RatioFixedImageView
        val mapErrorMsg: TextView

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
            googleMapImage = itemView.findViewById(R.id.google_map_image)
            mapErrorMsg = itemView.findViewById(R.id.error_msg)
        }
    }

    fun setComposeFooterInterface(composeFooterInterface: ComposeFooterInterface) {
        this.composeFooterInterface = composeFooterInterface
    }

    fun setInvokeGenericWebViewInterface(invokeGenericWebViewInterface: InvokeGenericWebViewInterface) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface
    }

    fun updateList(models: List<NearByStockAvailableStoreModel>, isLastItem: Boolean) {
        nearByStockAvailableStoreModels = models
        this.isLastItem = isLastItem
        notifyDataSetChanged()
    }
}