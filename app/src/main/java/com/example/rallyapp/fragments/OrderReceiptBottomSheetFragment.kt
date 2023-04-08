package com.example.rallyapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rallyapp.api.dataModel.response_models.Order
import com.example.rallyapp.api.dataModel.response_models.User
import com.example.rallyapp.databinding.FragmentOrderReceiptBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
    }
}