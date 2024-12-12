package com.capstone.free.education.data.remote.retrofit

data class SelfCheckRequest(
    var newRegister: Int,
    var transaction_amount: Int,
    var user_total_cashout: Int,
    var company_total_cashout: Int,
    var user_total_balance: Int
)
