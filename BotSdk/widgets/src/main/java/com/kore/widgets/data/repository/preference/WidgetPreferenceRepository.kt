package com.kore.widgets.data.repository.preference

import android.content.Context

interface WidgetPreferenceRepository {

    fun getStringValue(context: Context, fileName: String? = null, key: String, defaultValue: String = ""): String

    fun putStringValue(context: Context, fileName: String? = null, key: String, value: String)

    fun getIntValue(context: Context, fileName: String? = null, key: String, defaultValue: Int = -1): Int

    fun putIntValue(context: Context, fileName: String? = null, key: String, value: Int)

    fun getLongValue(context: Context, fileName: String? = null, key: String, defaultValue: Long = -1): Long

    fun putLongValue(context: Context, fileName: String? = null, key: String, value: Long)

    fun getBooleanValue(context: Context, fileName: String? = null, key: String, defaultValue: Boolean = false): Boolean

    fun putBooleanValue(context: Context, fileName: String? = null, key: String, value: Boolean)

    fun getStringSetValues(context: Context, fileName: String? = null, key: String, defaultValues: Set<String> = emptySet()): Set<String>

    fun putStringSetValues(context: Context, fileName: String? = null, key: String, values: Set<String>)

    fun removeValueWithKey(context: Context, fileName: String? = null, key: String)

    fun removeValueWithFileName(context: Context, fileName: String)

    fun getAll(context: Context, fileName: String): Map<String, *>

    fun hasValue(context: Context, fileName: String?, key: String): Boolean
}