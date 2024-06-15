package dev.smai1e.carTrader.utils

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

interface CallProvider {

    fun Fragment.callByPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
        startActivity(intent)
    }
}