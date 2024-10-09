package com.kore.validator

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints
import java.util.Calendar

internal class RangeValidator : CalendarConstraints.DateValidator {
    private val minDate: Long?
    private val maxDate: Long?

    constructor(minDate: Long?, maxDate: Long?) {
        this.minDate = minDate
        this.maxDate = maxDate
    }

    constructor(parcel: Parcel) {
        minDate = parcel.readLong()
        maxDate = parcel.readLong()
    }

    override fun isValid(date: Long): Boolean {
        return if (minDate != null && maxDate != null) {
            !(minDate > date || maxDate < date)
        } else {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date
            return !calendar.before(Calendar.getInstance())
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
    }

    companion object CREATOR : Parcelable.Creator<RangeValidator> {
        override fun createFromParcel(parcel: Parcel): RangeValidator {
            return RangeValidator(parcel)
        }

        override fun newArray(size: Int): Array<RangeValidator?> {
            return arrayOfNulls(size)
        }
    }
}