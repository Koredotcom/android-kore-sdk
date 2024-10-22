package com.kore.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewDataBinding, N : BaseView, V : BaseViewModel<N>> : Fragment(),
    BaseView {
    protected var binding: B? = null
    private var mViewModel: V? = null

    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<B>(inflater, getLayoutID(), container, false)
        this.binding = binding
        this.mViewModel = getViewModel()
        this.binding?.setVariable(getBindingVariable(), mViewModel)
        mViewModel?.setView(this as N?)
        this.binding?.lifecycleOwner = this

        return binding.root
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}