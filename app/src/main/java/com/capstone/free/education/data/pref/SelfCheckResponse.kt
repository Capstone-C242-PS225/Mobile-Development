package com.capstone.free.education.data.pref

data class SelfCheckResponse(
    var newRegister: Boolean = false,
    var transactionAmount: Double = 0.0,
    var userTotalCashout: Double = 0.0,
    var companyTotalCashout: Double = 0.0,
    var userTotalBalance: Double = 0.0,
    var profitOrLoss: String = "",
    var amount: Double = 0.0
)
