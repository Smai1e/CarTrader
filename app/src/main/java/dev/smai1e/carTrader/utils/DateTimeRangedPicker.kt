package dev.smai1e.carTrader.utils

import androidx.fragment.app.FragmentManager
import dev.smai1e.carTrader.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

enum class Picker {
    DATE,
    DATE_TIME
}

class DateTimeRangedPicker(
    builder: Builder
) {

    private var _openingDate: Long = 0L
    private var _closingDate: Long = 0L
    private val childFragmentManager = builder.childFragmentManager
    private val onPositiveButtonClickAction = builder.onPositiveButtonClickAction
    private val pickerType: Picker = builder.pickerType
    private val constraintsBuilder = CalendarConstraints.Builder()
        .setValidator(DateValidatorPointForward.now())
        .build()
    private val dateRangePicker = MaterialDatePicker.Builder
        .dateRangePicker()
        .setTitleText(R.string.trading_date)
        .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        .setCalendarConstraints(constraintsBuilder)
        .build()

    init {
        openDatePickerDialog()
    }

    class Builder {
        lateinit var childFragmentManager: FragmentManager
            private set
        var onPositiveButtonClickAction: ((Pair<String, String>) -> Unit)? = null
            private set
        var pickerType: Picker = Picker.DATE
            private set

        fun childFragmentManager(childFragmentManager: FragmentManager) = apply {
            this.childFragmentManager = childFragmentManager
        }

        fun positiveButtonClickAction(action: (Pair<String, String>) -> Unit) = apply {
            this.onPositiveButtonClickAction = action
        }

        fun pickerType(pickerType: Picker) = apply {
            this.pickerType = pickerType
        }

        fun build(): DateTimeRangedPicker = DateTimeRangedPicker(this)
    }

    private fun openDatePickerDialog() {
        dateRangePicker.show(childFragmentManager, "datePicker")
        dateRangePicker.addOnPositiveButtonClickListener { dateRange ->
            datePickerPositiveAction(dateRange.first, dateRange.second)
        }
    }

    private fun openTimePickerDialog(isOpeningTime: Boolean) {

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTitleText(
                if (isOpeningTime)
                    R.string.trading_start_time
                else
                    R.string.trading_end_time
            ).build()

        timePicker.show(childFragmentManager, "timePicker")
        timePicker.addOnPositiveButtonClickListener {
            timePickerPositiveAction(timePicker, isOpeningTime)
        }
        timePicker.addOnCancelListener {
            _openingDate = 0L
            _closingDate = 0L
        }
        timePicker.addOnNegativeButtonClickListener {
            _openingDate = 0L
            _closingDate = 0L
        }
    }

    private fun timePickerPositiveAction(
        timePicker: MaterialTimePicker,
        isOpeningTime: Boolean
    ) {
        val hourToMillils = timePicker.hour * 3600000L
        val minuteToMillils = timePicker.minute * 60000L

        if (isOpeningTime) {
            _openingDate = _openingDate.plus(hourToMillils + minuteToMillils)
            openTimePickerDialog(false)
        } else {
            _closingDate = _closingDate.plus(hourToMillils + minuteToMillils)
            returnDateRange(_openingDate, _closingDate)
        }
    }

    private fun datePickerPositiveAction(openingDate: Long, closingDate: Long) {
        when (pickerType) {
            Picker.DATE -> {
                returnDateRange(openingDate, closingDate)
            }

            Picker.DATE_TIME -> {
                _openingDate = openingDate
                _closingDate = closingDate
                openTimePickerDialog(true)
            }
        }
    }

    private fun returnDateRange(openingDate: Long, closingDate: Long) {
        onPositiveButtonClickAction?.let { action ->
            action(openingDate.convertEpochTimeMillisToISO8601() to closingDate.convertEpochTimeMillisToISO8601())
        }
    }
}