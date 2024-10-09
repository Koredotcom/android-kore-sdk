package com.kore.network.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.internal.bind.util.ISO8601Utils
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import io.gsonfire.GsonFireBuilder
import java.io.IOException
import java.sql.Date
import java.text.DateFormat
import java.text.ParseException
import java.text.ParsePosition

/**
 * Copyright (c) 2023 Kore Inc. All rights reserved.
 */
class ApiJson {
    /**
     * Get Gson.
     *
     * @return Gson
     */
    var gson: Gson
        private set

    init {
        gson = createGson()
            .registerTypeAdapter(java.util.Date::class.java, DateTypeAdapter())
            .registerTypeAdapter(Date::class.java, SqlDateTypeAdapter())
            .create()
    }

    /**
     * Set Gson.
     *
     * @param gson Gson
     * @return JSON
     */
    fun setGson(gson: Gson): ApiJson {
        this.gson = gson
        return this
    }

    /**
     * Gson TypeAdapter for java.sql.Date type
     * If the dateFormat is null, a simple "yyyy-MM-dd" format will be used
     * (more efficient than SimpleDateFormat).
     */
    class SqlDateTypeAdapter : TypeAdapter<Date?>() {
        private var dateFormat: DateFormat? = null
        fun setFormat(dateFormat: DateFormat?) {
            this.dateFormat = dateFormat
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, date: Date?) {
            if (date == null) {
                out.nullValue()
            } else {
                val value: String = if (dateFormat != null) {
                    dateFormat?.format(date) ?: ""
                } else {
                    date.toString()
                }
                out.value(value)
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): Date? {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull()
                return null
            }
            val date = reader.nextString()
            return try {
                if (dateFormat != null) {
                    dateFormat?.let { format -> format.parse(date)?.let { Date(it.time) } }
                } else Date(ISO8601Utils.parse(date, ParsePosition(0)).time)
            } catch (e: ParseException) {
                throw JsonParseException(e)
            }
        }
    }

    /**
     * Gson TypeAdapter for java.util.Date type
     * If the dateFormat is null, ISO8601Utils will be used.
     */
    class DateTypeAdapter : TypeAdapter<java.util.Date?>() {
        private var dateFormat: DateFormat? = null
        fun setFormat(dateFormat: DateFormat?) {
            this.dateFormat = dateFormat
        }

        @Throws(IOException::class)
        override fun write(out: JsonWriter, date: java.util.Date?) {
            if (date == null) {
                out.nullValue()
            } else {
                val value: String = if (dateFormat != null) {
                    dateFormat?.format(date) ?: ""
                } else {
                    ISO8601Utils.format(date, true)
                }
                out.value(value)
            }
        }

        @Throws(IOException::class)
        override fun read(reader: JsonReader): java.util.Date? {
            return try {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return null
                }
                val date = reader.nextString()
                try {
                    if (dateFormat != null) {
                        dateFormat?.parse(date)
                    } else ISO8601Utils.parse(date, ParsePosition(0))
                } catch (e: ParseException) {
                    throw JsonParseException(e)
                }
            } catch (e: IllegalArgumentException) {
                throw JsonParseException(e)
            }
        }
    }

    companion object {
        fun createGson(): GsonBuilder {
            val fireBuilder = GsonFireBuilder()
            return fireBuilder.createGsonBuilder()
        }
    }
}