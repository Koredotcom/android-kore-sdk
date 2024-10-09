package com.kore.ui.audiocodes.webrtcclient.general

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import java.util.Locale

@SuppressLint("UnknownNullness")
open class BasePrefs {
    companion object {
        private const val TAG = "BasePrefs"
        private const val PREFS_NAME = "PREFS_WEBRTC"

        // basic functions
        // int
        @JvmStatic
        fun putInt(context: Context, prefKey: String, value: Int) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            editorSP.putInt(key, value)
            editorSP.commit()
        }

        @JvmStatic
        fun getInt(context: Context, key: String): Int {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            return getInt(context, key, -1)
        }

        @JvmStatic
        fun getInt(context: Context, prefKey: String, defValue: Int): Int {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            return sharedPreferences.getInt(key, defValue)
        }

        fun getLong(context: Context, prefKey: String, defValue: Long): Long {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            return sharedPreferences.getLong(key, defValue)
        }

        // string
        @JvmStatic
        fun putString(context: Context, prefKey: String, value: String?) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            editorSP.putString(key, value)
            editorSP.commit()
        }

        @JvmStatic
        fun getString(context: Context, prefKey: String): String? {
            return getString(context, prefKey, null)
        }

        @JvmStatic
        fun getString(context: Context, prefKey: String, defValue: String?): String? {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            return sharedPreferences.getString(key, defValue)
        }

        // boolean
        @JvmStatic
        fun putBoolean(context: Context, prefKey: String, value: Boolean) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            Log.d(TAG, "putBoolean: $key value: $value")
            editorSP.putBoolean(key, value)
            editorSP.commit()
        }

        @JvmStatic
        fun getBoolean(context: Context, key: String): Boolean {
            return getBoolean(context, key, false)
        }

        @JvmStatic
        fun getBoolean(context: Context, prefKey: String, defValue: Boolean): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            val value = sharedPreferences.getBoolean(key, defValue)
            Log.d(TAG, "getBoolean: $key vlaue: $value defValue: $defValue")
            return value
        }

        // boolean
        @JvmStatic
        fun putClass(context: Context, prefKey: String, value: Any?) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editorSP = sharedPreferences.edit()
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            val gson = Gson()
            val json = gson.toJson(value)
            Log.d(TAG, "putClass: $key vlaue: $json")
            editorSP.putString(key, json)
            editorSP.commit()
        }

        @JvmStatic
        fun getClass(context: Context, key: String, cls: Class<*>?): Any? {
            return getClass(context, key, cls, null)
        }

        @JvmStatic
        fun getClass(context: Context, prefKey: String, cls: Class<*>?, defValue: Any?): Any? {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            var key = prefKey
            key = key.lowercase(Locale.getDefault())
            val gson = Gson()
            val json = sharedPreferences.getString(key, "")
            if (json == "") {
                return defValue
            }
            val value = gson.fromJson<Any>(json, cls)
            Log.d(TAG, "getBoolean: $key vlaue: $value defValue: $defValue")
            return value
        }
    }
}
