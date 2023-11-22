package kore.botssdk.utils

import android.annotation.SuppressLint
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar

class DatePickerUtil {
    companion object {
        private const val LOG_TAG = "DatePickerUtil"
        fun showRangeDatePicker(title: String, fragmentManager: FragmentManager, onDateSelected: (selectedDates: String) -> Unit) {
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorFuture())
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText(title)
            builder.setCalendarConstraints(constraintsBuilder.build())
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            builder.setTheme(kore.botssdk.R.style.MyMaterialCalendarTheme)
            try {
                val picker = builder.build()
                picker.show(fragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { selection ->
                    val startDate = selection.first
                    val endDate = selection.second
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = startDate
                    val strYear = cal[Calendar.YEAR]
                    val strMonth = cal[Calendar.MONTH]
                    val strDay = cal[Calendar.DAY_OF_MONTH]
                    cal.timeInMillis = endDate
                    val endYear = cal[Calendar.YEAR]
                    val endMonth = cal[Calendar.MONTH]
                    val endDay = cal[Calendar.DAY_OF_MONTH]
                    var formattedDate =
                        DateUtils.getMonthName(strMonth) + " " + strDay + DateUtils.getDayOfMonthSuffix(
                            strDay
                        ) + ", " + strYear
                    formattedDate =
                        formattedDate + " to " + DateUtils.getMonthName(endMonth) + " " + endDay + DateUtils.getDayOfMonthSuffix(
                            endDay
                        ) + ", " + endYear
                    if (formattedDate.isNotEmpty()) onDateSelected(formattedDate)
                }
            } catch (e: IllegalArgumentException) {
                LogUtils.e(LOG_TAG, e.message)
            }
        }

        fun showDatePicker(title: String, fragmentManager: FragmentManager, onDatesSelected: (selectedDates: String) -> Unit) {
            val constraintsBuilder = CalendarConstraints.Builder()
            constraintsBuilder.setValidator(DateValidatorFuture())
            val builder = MaterialDatePicker.Builder.datePicker()
            builder.setTitleText(title)
            builder.setCalendarConstraints(constraintsBuilder.build())
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            builder.setTheme(kore.botssdk.R.style.MyMaterialCalendarTheme)
            try {
                val picker = builder.build()
                picker.show(fragmentManager, picker.toString())
                picker.addOnPositiveButtonClickListener { selection ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = selection!!
                    val stYear = calendar[Calendar.YEAR]
                    val stMonth = calendar[Calendar.MONTH]
                    val stDay = calendar[Calendar.DAY_OF_MONTH]
                    val formattedDate = ((if (stMonth + 1 > 9) stMonth + 1 else "0" + (stMonth + 1))
                        .toString() + "-" + (if (stDay > 9) stDay else "0$stDay")
                            + "-" + stYear)
                    if (formattedDate.isNotEmpty()) onDatesSelected(formattedDate)
                }
            } catch (e: IllegalArgumentException) {
                LogUtils.e(LOG_TAG, e.message)
            }
        }
    }
}

// Custom date validator to allow only future dates
@SuppressLint("ParcelCreator")
class DateValidatorFuture : CalendarConstraints.DateValidator {
    override fun isValid(date: Long): Boolean {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        return !calendar.before(Calendar.getInstance())
    }

    override fun writeToParcel(p0: android.os.Parcel, p1: Int) {}
    override fun describeContents(): Int = 0
}