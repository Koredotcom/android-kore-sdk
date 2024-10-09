package com.kore.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference


open class BaseViewModel<N : BaseView>() : ViewModel() {

    private var view: WeakReference<N?> = WeakReference(null)

    internal fun setView(view: N?) {
        this.view = WeakReference(view)
    }

    fun getView() = view.get()

    internal fun Job?.cancelIfActive() = this?.takeIf { it.isActive }?.cancel()
}