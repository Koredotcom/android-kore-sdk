package kore.botssdk.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kore.botssdk.adapter.NearByStockAvailableStoresAdapter
import kore.botssdk.extensions.dpToPx
import kore.botssdk.listener.ComposeFooterInterface
import kore.botssdk.listener.InvokeGenericWebViewInterface
import kore.botssdk.models.NearByStockAvailableStoreModel
import kore.botssdk.view.NearByStockAvailableStoreListView
import kore.botssdk.view.viewUtils.LayoutUtils
import kore.botssdk.view.viewUtils.MeasureUtils

class NearByStockAvailableStoreListView : ViewGroup {
    private var restrictedMaxWidth = 0f
    private var composeFooterInterface: ComposeFooterInterface? = null
    private var invokeGenericWebViewInterface: InvokeGenericWebViewInterface? = null
    private var recyclerView: RecyclerView? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        recyclerView = RecyclerView(context)
        addView(recyclerView)
    }

    fun populateNearByStockAvailableStores(models: List<NearByStockAvailableStoreModel>?) {
        if (!models.isNullOrEmpty()) {
            recyclerView?.visibility = GONE
            val adapter: NearByStockAvailableStoresAdapter?
            if (recyclerView?.adapter == null) {
                adapter = NearByStockAvailableStoresAdapter(context, models)
                recyclerView!!.adapter = adapter
                composeFooterInterface?.let { adapter.setComposeFooterInterface(it) }
                invokeGenericWebViewInterface?.let { adapter.setInvokeGenericWebViewInterface(it) }
            } else {
                adapter = recyclerView?.adapter as NearByStockAvailableStoresAdapter
            }
            adapter.updateList(models)
            recyclerView?.visibility = VISIBLE
        } else {
            recyclerView?.visibility = GONE
        }
    }

    fun setRestrictedMaxWidth(restrictedMaxWidth: Float) {
        this.restrictedMaxWidth = restrictedMaxWidth
    }

    fun setComposeFooterInterface(composeFooterInterface: ComposeFooterInterface?) {
        this.composeFooterInterface = composeFooterInterface
    }

    fun setInvokeGenericWebViewInterface(invokeGenericWebViewInterface: InvokeGenericWebViewInterface?) {
        this.invokeGenericWebViewInterface = invokeGenericWebViewInterface
    }

    val viewWidth: Int
        get() {
            var viewHeight = 0
            if (recyclerView != null) {
                if (recyclerView?.adapter != null) {
                    recyclerView?.adapter?.itemCount?.let {
                        viewHeight = if (it > 0) (restrictedMaxWidth * 1.dpToPx(context)).toInt() else 0
                    }
                }

            }
            return viewHeight
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        var totalHeight = paddingTop
        val childWidthSpec: Int
        var totalWidth = paddingLeft
        childWidthSpec = MeasureSpec.makeMeasureSpec(restrictedMaxWidth.toInt(), MeasureSpec.EXACTLY)
        MeasureUtils.measure(recyclerView, childWidthSpec, wrapSpec)
        recyclerView?.let {
            totalHeight += it.measuredHeight + paddingBottom + paddingTop
            totalWidth += it.measuredWidth + paddingLeft + paddingRight
        }

        if (totalHeight != 0) {
            totalWidth += (3 * 1.dpToPx(context)) as Int
        }
        val parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY)
        val parentWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST)
        setMeasuredDimension(parentWidthSpec, parentHeightSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val parentWidth = measuredWidth

        //get the available size of child view
        val childLeft = 0
        var childTop = 0
        val itemWidth = (r - l) / childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop)
                childTop += child.measuredHeight
            }
        }
    }
}