package dev.smai1e.carTrader.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText

interface TextWatcherProvider {

    fun EditText.setOnTextChangeListener(block: (text: Editable) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(text: Editable) {
                try {
                    block(text)
                } catch (e: Exception) {
                    Log.e("TextWatcher", e.message, e)
                }
            }
        })
    }

    fun EditText.addTextWatcher(watcher: TextWatcher) {
        this.addTextChangedListener(watcher)
    }
}