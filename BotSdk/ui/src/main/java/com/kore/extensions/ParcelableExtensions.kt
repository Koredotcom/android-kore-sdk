package com.kore.extensions

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? =
    if (SDK_INT >= TIRAMISU) getParcelableExtra(key, T::class.java) else @Suppress("DEPRECATION") getParcelableExtra(key) as? T

inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? =
    if (SDK_INT >= TIRAMISU) getParcelable(key, T::class.java) else @Suppress("DEPRECATION") getParcelable(key) as? T