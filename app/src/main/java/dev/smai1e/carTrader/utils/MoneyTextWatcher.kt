package dev.smai1e.carTrader.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import java.util.Objects

class MoneyTextWatcher(editText: EditText? = null) : TextWatcher {

    private val editTextWeakReference: WeakReference<EditText>

    init {
        editTextWeakReference = WeakReference(editText)
        formatter.maximumFractionDigits = 0
        formatter.roundingMode = RoundingMode.FLOOR
        val symbol = DecimalFormatSymbols(locale)
        symbol.currencySymbol = symbol.currencySymbol + " "
        formatter.decimalFormatSymbols = symbol
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        val editText = editTextWeakReference.get() ?: return
        editText.removeTextChangedListener(this)

        val parsed: BigDecimal = if (editText.text.toString().isEmpty()) {
            parseCurrencyValue("0")
        } else {
            parseCurrencyValue(editText.text.toString())
        }
        val formatted: String = formatter.format(parsed)
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    companion object {
        private val locale: Locale = Locale("ru")
        private val formatter: DecimalFormat =
            NumberFormat.getInstance(locale) as DecimalFormat

        fun parseCurrencyValue(value: String): BigDecimal {
            try {
                val replaceRegex = java.lang.String.format(
                    "[%s,.\\s]",
                    Objects.requireNonNull(formatter.currency).getSymbol(locale)
                )
                var currencyValue = value.replace(replaceRegex.toRegex(), "")
                currencyValue = if ("" == currencyValue) "0" else currencyValue
                return BigDecimal(currencyValue)
            } catch (e: Exception) {
                Log.e("MoneyTextWatcher", e.message, e)
            }
            return BigDecimal.ZERO
        }
    }
}

fun Editable.getAmount(): Int {
    return MoneyTextWatcher.parseCurrencyValue(this.toString()).toInt()
}