package com.kore.ui.base

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<B : ViewDataBinding, N : BaseView, V : BaseViewModel<N>> : DialogFragment(), BaseView {
    protected lateinit var binding: B
    private var mViewModel: V? = null
    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V

    private fun setWidthPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        val percentHeight = rect.height() * percent / 2
        dialog?.window?.setLayout(percentWidth.toInt(), percentHeight.toInt())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate<B>(inflater, getLayoutID(), container, false)
        this.mViewModel = getViewModel()
        this.binding.setVariable(getBindingVariable(), mViewModel)
        mViewModel?.setView(this as N?)
        this.binding.lifecycleOwner = this
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
    }
}