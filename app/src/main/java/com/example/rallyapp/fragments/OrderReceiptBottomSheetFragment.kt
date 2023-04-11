package com.example.rallyapp.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.databinding.FragmentOrderReceiptBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Float

class OrderReceiptBottomSheetFragment(private val order: Order<User>): BottomSheetDialogFragment() {

    private var _binding: FragmentOrderReceiptBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrderReceiptBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.orderReceiptBottomSheetTotalPriceValue.text = "$${order.beforeTaxPrice}"
        binding.orderReceiptBottomSheetTaxPriceValue.text = "$${order.taxPrice}"
        binding.orderReceiptBottomSheetGrandTotalValue.text = "$${order.totalPrice}"

        binding.orderReceiptBottomSheetDismissButton.setOnClickListener {
            dismiss()
        }
        setVoucher()
    }

    private fun setVoucher() {
        order.voucher?.let {
            binding.orderReceiptBottomSheetOfferDescriptionSection.visibility = View.VISIBLE
            addStrikeTexOnTextView(
                binding.orderReceiptBottomSheetTotalPriceValue,
                "$${order.beforeTaxPrice} $${order.afterOfferPrice}",
                "$${order.beforeTaxPrice}"
            )
            binding.orderReceiptBottomSheetOfferDescriptionLabel.text =
                "${it.offerPercent}% off saved $${
                    Float.valueOf(order.beforeTaxPrice) - Float.valueOf(order.afterOfferPrice)
                }"
        }
    }

    private fun addStrikeTexOnTextView(tv: TextView, fullText: String, strokedSubText: String) {
        val spannable = SpannableString(fullText)
        val strikeThroughSpan = StrikethroughSpan()

        val startIndex = fullText.indexOf(strokedSubText)
        val endIndex = startIndex + strokedSubText.length

        spannable.setSpan(strikeThroughSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv.text = spannable
    }
}