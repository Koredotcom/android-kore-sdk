package com.kore.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding, N : BaseView, V : BaseViewModel<N>> : AppCompatActivity(),
    BaseView {
    private var mViewModel: V? = null
    protected lateinit var binding: B

    abstract fun getLayoutID(): Int
    abstract fun getBindingVariable(): Int
    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(LayoutInflater.from(this@BaseActivity), getLayoutID(), null, false)
        this.mViewModel = getViewModel()
        if (getBindingVariable() != -1) binding.setVariable(getBindingVariable(), mViewModel)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        mViewModel?.setView(this as? N)
    }
}