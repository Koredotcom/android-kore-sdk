package com.kore.widgets.data.repository.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.preference.PreferenceManager

class WidgetPreferenceRepositoryImpl : WidgetPreferenceRepository {

    override fun getStringValue(context: Context, fileName: String?, key: String, defaultValue: String): String =
        getSharedPreference(context, fileName).getString(key, defaultValue).orEmpty()

    override fun putStringValue(context: Context, fileName: String?, key: String, value: String) {
        getSharedPreference(context, fileName).edit().putString(key, value).apply()
    }

    override fun getIntValue(context: Context, fileName: String?, key: String, defaultValue: Int): Int =
        getSharedPreference(context, fileName).getInt(key, defaultValue)

    override fun putIntValue(context: Context, fileName: String?, key: String, value: Int) {
        getSharedPreference(context, fileName).edit().putInt(key, value).apply()
    }

    override fun getLongValue(context: Context, fileName: String?, key: String, defaultValue: Long): Long =
        getSharedPreference(context, fileName).getLong(key, defaultValue)

    override fun putLongValue(context: Context, fileName: String?, key: String, value: Long) {
        getSharedPreference(context, fileName).edit().putLong(key, value).apply()
    }

    override fun getBooleanValue(context: Context, fileName: String?, key: String, defaultValue: Boolean): Boolean =
        getSharedPreference(context, fileName).getBoolean(key, defaultValue)

    override fun putBooleanValue(context: Context, fileName: String?, key: String, value: Boolean) {
        getSharedPreference(context, fileName).edit().putBoolean(key, value).apply()
    }

    override fun getStringSetValues(context: Context, fileName: String?, key: String, defaultValues: Set<String>): Set<String> =
        getSharedPreference(context, fileName).getStringSet(key, defaultValues).orEmpty()

    override fun putStringSetValues(context: Context, fileName: String?, key: String, values: Set<String>) {
        getSharedPreference(context, fileName).edit().putStringSet(key, values).apply()
    }

    override fun removeValueWithKey(context: Context, fileName: String?, key: String) {
        getSharedPreference(context, fileName).edit().remove(key).apply()
    }

    override fun removeValueWithFileName(context: Context, fileName: String) {
        getSharedPreference(context, fileName).edit().clear().apply()
    }

    override fun hasValue(context: Context, fileName: String?, key: String): Boolean = getSharedPreference(context, fileName).contains(key)

    override fun getAll(context: Context, fileName: String): Map<String, *> = getSharedPreference(context, fileName).all

    fun getSharedPreference(context: Context, fileName: String?): SharedPreferences =
        if (fileName != null) context.getSharedPreferences(fileName, MODE_PRIVATE)
        else PreferenceManager.getDefaultSharedPreferences(context)
}