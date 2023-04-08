package com.example.rallyapp.repo

import com.example.rallyapp.api.api_helpers.VoucherApiHelper
import com.example.rallyapp.api.dataModel.response_models.ApiResponse
import com.example.rallyapp.api.dataModel.response_models.Voucher

class VoucherRepo {

    private val voucherApiHelper = VoucherApiHelper()

    fun getUsersVouchers(
        userId: Int,
        token: String,
        callback: (ApiResponse<Voucher>) -> Unit
    ){
        voucherApiHelper.getUsersVoucher(userId, token){
            callback(it)
        }
    }
}