package dev.smai1e.carTrader.ui.views

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieAnimationView
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.ui.State
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class BaseDialogFragment : BottomSheetDialogFragment() {

    fun <T : Any> State<T>.onIdle(
        block: () -> Unit
    ): State<T> = apply {
        if (this is State.Idle) {
            block()
        }
    }

    fun <T : Any> State<T>.onLoading(
        block: () -> Unit
    ): State<T> = apply {
        if (this is State.Loading) {
            block()
        }
    }

    fun <T : Any> State<T>.onSuccess(
        block: (data: T) -> Unit
    ): State<T> = apply {
        if (this is State.Success<T>) {
            block(this.data)
        }
    }

    fun <T : Any> State<T>.onError(
        block: (error: String) -> Unit
    ): State<T> = apply {
        if (this is State.Error<T>) {
            block(getErrorText(requireContext()))
        }
    }

    fun <T : Any> State<T>.andExecute(
        block: () -> Unit
    ): State<T> = apply {
        block()
    }

    fun <T> Flow<T>.collectFlow(onCollect: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@collectFlow.collect {
                    onCollect(it)
                }
            }
        }
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    fun showSuccessSnackbar(@StringRes descriptionId: Int) {
        showSnackbar(
            getString(R.string.success),
            getString(descriptionId),
            R.raw.success
        )
    }

    fun showSuccessSnackbar(description: String) {
        showSnackbar(
            getString(R.string.success),
            description,
            R.raw.success
        )
    }

    fun showErrorSnackbar(@StringRes descriptionId: Int) {
        showSnackbar(
            getString(R.string.error),
            getString(descriptionId),
            R.raw.error
        )
    }

    fun showErrorSnackbar(description: String) {
        showSnackbar(
            getString(R.string.error),
            description,
            R.raw.error
        )
    }

    private fun showSnackbar(title: String, description: String, @RawRes imageId: Int) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            "",
            Snackbar.LENGTH_SHORT
        )

        val customView = layoutInflater.inflate(R.layout.snackbar_layout, null)
        val imageView = customView.findViewById<LottieAnimationView>(R.id.imageView)
        val titleTextView = customView.findViewById<TextView>(R.id.textView_title)
        val descriptionTextView = customView.findViewById<TextView>(R.id.textView_description)

        imageView.setAnimation(imageId)
        titleTextView.text = title
        descriptionTextView.text = description

        snackbar.view.setBackgroundColor(android.graphics.Color.TRANSPARENT)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customView, 0)
        snackbar.show()
    }
}