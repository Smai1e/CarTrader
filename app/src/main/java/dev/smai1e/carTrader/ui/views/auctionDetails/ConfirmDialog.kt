package dev.smai1e.carTrader.ui.views.auctionDetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import dev.smai1e.carTrader.R
import dev.smai1e.carTrader.databinding.ConfirmDialogBinding
import dev.smai1e.carTrader.utils.findTopNavController
import dev.smai1e.carTrader.utils.toCurrencyString

class ConfirmDialog : DialogFragment() {

    private var _binding: ConfirmDialogBinding? = null
    private val binding get() = _binding!!

    private val args: ConfirmDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ConfirmDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val bidAmount = args.bidAmount
        binding.dialogDescription.text = getString(R.string.exactly_want_to_bid, bidAmount.toCurrencyString())
        binding.confirmButton.setOnClickListener {
            setFragmentResult(
                REQUEST_KEY,
                bundleOf(EXTRA_KEY to true)
            )
            goBack()
        }
        binding.cancellationButton.setOnClickListener { goBack() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun goBack() = findTopNavController().popBackStack()

    companion object {
        const val REQUEST_KEY = "confirm_dialog_result"
        const val EXTRA_KEY = "is_confirm"
    }
}