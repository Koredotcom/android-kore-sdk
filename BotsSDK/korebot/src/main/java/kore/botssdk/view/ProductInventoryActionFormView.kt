package kore.botssdk.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import kore.botssdk.R
import kore.botssdk.databinding.ProductInventoryActionFormBinding
import kore.botssdk.extensions.dpToPx
import kore.botssdk.listener.ComposeFooterInterface
import kore.botssdk.view.viewUtils.LayoutUtils
import kore.botssdk.view.viewUtils.MeasureUtils

class ProductInventoryActionFormView : ViewGroup {
    companion object {
        private val REGEX_PHONE_NUMBER_VALIDATION = "\\+[0-9]{9,13}".toRegex()
        private val REGEX_EMAIL_ADDRESS = "^([\\w-_\\.]{3,40})+@(([\\w-]{2,20})+\\.)+[\\w-]{2,20}\$".toRegex()
        private const val REQUEST_FORM_DATA = "userName:%s|phoneNumber:%s|email:%s|itemId:%s|actionName:%s"
    }

    private var restrictedMaxWidth = 0f
    private var composeFooterInterface: ComposeFooterInterface? = null
    private lateinit var binding: ProductInventoryActionFormBinding
    private var selectedItemId: String? = null

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
        binding = ProductInventoryActionFormBinding.inflate(LayoutInflater.from(context), this, true)
        binding.actionSendDirections.setOnClickListener {
            submitAction(binding.actionSendDirections.text.toString())
        }
        binding.actionReserve.setOnClickListener {
            submitAction(binding.actionReserve.text.toString() + "s")
        }
    }

    private fun submitAction(action: String) {
        val isValidUserName = binding.userName.toString().trim().isNotEmpty()
        val isValidPhoneNumber = binding.phoneNumber.text.toString().trim().matches(REGEX_PHONE_NUMBER_VALIDATION)
        val isValidEmail = binding.email.text.toString().trim().matches(REGEX_EMAIL_ADDRESS)
        if (!isValidUserName) {
            Toast.makeText(context, context.getString(R.string.msg_invalid_user_name), Toast.LENGTH_LONG).show()
        } else if (!isValidPhoneNumber) {
            Toast.makeText(context, context.getString(R.string.msg_invalid_phone_number), Toast.LENGTH_LONG).show()
        } else if (!isValidEmail) {
            Toast.makeText(context, context.getString(R.string.msg_invalid_email), Toast.LENGTH_LONG).show()
        } else {
            selectedItemId?.let {
                val request = String.format(
                    REQUEST_FORM_DATA,
                    binding.userName.text.toString(),
                    binding.phoneNumber.text.toString(),
                    binding.email.text.toString(),
                    it,
                    action
                )
                composeFooterInterface?.onSendClick(request, false)
            }
        }
    }

    fun showInputForm(id: String?) {
        selectedItemId = id
        binding.userName.setText("")
        binding.phoneNumber.setText("")
        binding.email.setText("")
        binding.root.isVisible = !id.isNullOrEmpty()
    }

    fun setRestrictedMaxWidth(restrictedMaxWidth: Float) {
        this.restrictedMaxWidth = restrictedMaxWidth
    }

    fun setComposeFooterInterface(composeFooterInterface: ComposeFooterInterface?) {
        this.composeFooterInterface = composeFooterInterface
    }

    val viewWidth: Int
        get() {
            return if (binding.root.childCount > 0) (restrictedMaxWidth * 1.dpToPx(context)).toInt() else 0
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wrapSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        var totalHeight = paddingTop
        var totalWidth = paddingLeft
        val childWidthSpec: Int = MeasureSpec.makeMeasureSpec(restrictedMaxWidth.toInt(), MeasureSpec.EXACTLY)
        MeasureUtils.measure(binding.root, childWidthSpec, wrapSpec)
        totalHeight += binding.root.measuredHeight + paddingBottom + paddingTop
        totalWidth += binding.root.measuredWidth + paddingLeft + paddingRight

        if (totalHeight != 0) {
            totalWidth += (3 * 1.dpToPx(context))
        }
        val parentHeightSpec = MeasureSpec.makeMeasureSpec(totalHeight, MeasureSpec.EXACTLY)
        val parentWidthSpec = MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.AT_MOST)
        setMeasuredDimension(parentWidthSpec, parentHeightSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        val childLeft = 0
        var childTop = 0
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                LayoutUtils.layoutChild(child, childLeft, childTop)
                childTop += child.measuredHeight
            }
        }
    }
}