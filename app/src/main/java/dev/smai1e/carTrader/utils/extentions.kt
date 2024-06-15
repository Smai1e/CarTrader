package dev.smai1e.carTrader.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.smai1e.carTrader.R
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/**
 * Convert 5000 into "$5,000" (format depends on the localization)
 */
fun Int.toCurrencyString(): String {
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.currency = Currency.getInstance(Locale.getDefault())
    formatter.maximumFractionDigits = 0
    return formatter.format(this)
}

/**
 * Convert "12345678987" into "1 234 567-89-87"
 */
fun String.convertToFormattedPhoneNumber(): String {
    val regex = """(\d)(\d{3})(\d{3})(\d{2})(\d{2})""".toRegex()
    return regex.replace(this, "$1 $2 $3-$4-$5")
}

fun Fragment.findTopNavController(): NavController {
    val topLevelHost = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment?
    return topLevelHost?.navController ?: findNavController()
}

fun ImageView.loadFromUrlWithAnimation(url: String) =
    Glide.with(this)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade(150))
        .into(this)

fun ImageView.loadFromUrl(url: String) =
    Glide.with(this)
        .load(url)
        .into(this)

fun Intent.getClipDataUris(): ArrayList<Uri> {
    val resultSet = LinkedHashSet<Uri>()
    data?.let { data ->
        resultSet.add(data)
    }
    val clipData = clipData
    if (clipData == null && resultSet.isEmpty()) {
        return ArrayList()
    } else if (clipData != null) {
        for (i in 0 until clipData.itemCount) {
            val uri = clipData.getItemAt(i).uri
            if (uri != null) {
                resultSet.add(uri)
            }
        }
    }
    return ArrayList(resultSet)
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun Editable.toIntOrGetNull(): Int? {
    return try {
        this.toString().toInt()
    } catch (e: Exception) {
        null
    }
}