package com.kore.data.repository.preference

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.core.content.edit

class PreferenceRepositoryImpl : PreferenceRepository {

    override fun getStringValue(context: Context, fileName: String?, key: String, defaultValue: String): String =
        getSharedPreference(context, fileName).getString(key, defaultValue).orEmpty()

    override fun putStringValue(context: Context, fileName: String?, key: String, value: String?) {
        getSharedPreference(context, fileName).edit { putString(key, value) }
    }

    override fun getIntValue(context: Context, fileName: String?, key: String, defaultValue: Int): Int =
        getSharedPreference(context, fileName).getInt(key, defaultValue)

    override fun putIntValue(context: Context, fileName: String?, key: String, value: Int) {
        getSharedPreference(context, fileName).edit { putInt(key, value) }
    }

    override fun getLongValue(context: Context, fileName: String?, key: String, defaultValue: Long): Long =
        getSharedPreference(context, fileName).getLong(key, defaultValue)

    override fun putLongValue(context: Context, fileName: String?, key: String, value: Long) {
        getSharedPreference(context, fileName).edit { putLong(key, value) }
    }

    override fun getBooleanValue(context: Context, fileName: String?, key: String, defaultValue: Boolean): Boolean =
        getSharedPreference(context, fileName).getBoolean(key, defaultValue)

    override fun putBooleanValue(context: Context, fileName: String?, key: String, value: Boolean) {
        getSharedPreference(context, fileName).edit { putBoolean(key, value) }
    }

    override fun getStringSetValues(context: Context, fileName: String?, key: String, defaultValues: Set<String>): Set<String> =
        getSharedPreference(context, fileName).getStringSet(key, defaultValues).orEmpty()

    override fun putStringSetValues(context: Context, fileName: String?, key: String, values: Set<String>?) {
        getSharedPreference(context, fileName).edit { putStringSet(key, values) }
    }

    override fun removeValueWithKey(context: Context, fileName: String?, key: String) {
        getSharedPreference(context, fileName).edit { remove(key) }
    }

    override fun removeValueWithFileName(context: Context, fileName: String) {
        getSharedPreference(context, fileName).edit { clear() }
    }

    override fun hasValue(context: Context, fileName: String?, key: String): Boolean = getSharedPreference(context, fileName).contains(key)

    override fun getAll(context: Context, fileName: String): Map<String, *> = getSharedPreference(context, fileName).all

    fun getSharedPreference(context: Context, fileName: String?): SharedPreferences =
        if (fileName != null) context.getSharedPreferences(fileName, MODE_PRIVATE)
        else PreferenceManager.getDefaultSharedPreferences(context)
}