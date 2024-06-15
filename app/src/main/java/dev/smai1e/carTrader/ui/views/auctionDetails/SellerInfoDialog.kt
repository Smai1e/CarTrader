package dev.smai1e.carTrader.ui.views.auctionDetails

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import dev.smai1e.carTrader.databinding.SellerInfoDialogBinding
import dev.smai1e.carTrader.utils.CallProvider
import dev.smai1e.carTrader.utils.convertISO8601ToFormattedDate
import dev.smai1e.carTrader.utils.loadFromUrl

class SellerInfoDialog : DialogFragment(), CallProvider {

    private var _binding: SellerInfoDialogBinding? = null
    private val binding get() = _binding!!

    private val args: SellerInfoDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SellerInfoDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val sellerInfo = args.sellerInfo
        binding.apply {
            sellerImageView.loadFromUrl(sellerInfo.photoUrl)
            sellerFirstNameTextView.text = sellerInfo.firstName
            sellerMiddleNameTextView.text = sellerInfo.middleName
            sellerLastNameTextView.text = sellerInfo.lastName
            bidCountTextView.text = sellerInfo.bidsCount.toString()
            auctionCountTextView.text = sellerInfo.auctionsCount.toString()
            emailValueTextView.text = sellerInfo.email
            phoneNumberValueTextView.text = sellerInfo.phone
            registrationDateValueTextView.text = sellerInfo.registrationDate.convertISO8601ToFormattedDate()
            callFab.setOnClickListener {
                callByPhoneNumber(sellerInfo.phone)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}