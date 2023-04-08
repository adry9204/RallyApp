package com.example.rallyapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.rallyapp.api.dataModel.request_models.AddAddressRequest
import com.example.rallyapp.databinding.FragmentAddAddressBottomSheetBinding
import com.example.rallyapp.user.UserCredentials
import com.example.rallyapp.utils.AlertData
import com.example.rallyapp.utils.AlertManager
import com.example.rallyapp.viewModel.CheckoutActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout

class AddAddressBottomSheetFragment(private val viewModel: CheckoutActivityViewModel): BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AddAddressBottomSheetFragment"
    }

    private var _binding: FragmentAddAddressBottomSheetBinding ?= null
    private val  binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddAddressBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addAddressFragmentCancelButton.setOnClickListener {
            dismiss()
        }

        binding.addAddressFragmentSaveButton.setOnClickListener {
            if(verifyInputs()){
                val addAddressRequest = makeAddAddressRequest()
                viewModel.addNewAddress(addAddressRequest, UserCredentials.getToken()!!)
                dismiss()
            }
        }
    }

    private fun makeAddAddressRequest(): AddAddressRequest{
        return AddAddressRequest(
            city = getTextFromTextInput(binding.addAddressFragmentCity),
            country = "Canada",
            line1 = getTextFromTextInput(binding.addAddressFragmentLineOne),
            line2 = getTextFromTextInput(binding.addAddressFragmentLineTwo).ifEmpty { null },
            name = getTextFromTextInput(binding.addAddressFragmentName),
            postalCode = getTextFromTextInput(binding.addAddressFragmentPostalCode),
            province = "Ontario",
            userId = UserCredentials.getUserId()!!
        )
    }

    private fun verifyInputs(): Boolean{
        if(isEditTextEmpty(binding.addAddressFragmentName)){
            showMissingFieldAlert("name")
            return false
        }

        if(isEditTextEmpty(binding.addAddressFragmentLineOne)){
            showMissingFieldAlert("address line 1", needA = false)
            return false
        }

        if(isEditTextEmpty(binding.addAddressFragmentCity)){
            showMissingFieldAlert("city")
            return false
        }

        if(isEditTextEmpty(binding.addAddressFragmentPostalCode)){
            showMissingFieldAlert("postal code")
            return false
        }
        return true
    }

    private fun getTextFromTextInput(textInput: TextInputLayout): String{
        textInput.editText?.let {
            return it.text.toString()
        }
        return ""
    }

    private fun isEditTextEmpty(textInput: TextInputLayout): Boolean{
        textInput.editText?.let {
            return it.text.isEmpty()
        }
        return true
    }

    private fun showMissingFieldAlert(fieldName: String, needA: Boolean = true){
        val alertManager = AlertManager(activity?.applicationContext!!)
        alertManager.showAlertWithOkButton(AlertData(
            title = "Missing field",
            message = "Please enter ${if(needA) "a" else ""} $fieldName"
        ))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}