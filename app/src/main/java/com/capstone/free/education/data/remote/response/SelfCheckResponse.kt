package com.capstone.free.education.data.remote.response

import com.google.gson.annotations.SerializedName

data class SelfCheckResponse(

    @field:SerializedName("user_total_balance")
    val userTotalBalance: Int? = null,

    @field:SerializedName("company_total_cashout")
    val companyTotalCashout: Int? = null,

    @field:SerializedName("user_total_cashout")
    val userTotalCashout: Int? = null,

    @field:SerializedName("newRegister")
    val newRegister: Int? = null,

    @field:SerializedName("transaction_amount")
    val transactionAmount: Int? = null
)