package com.capstone.free.education.data.remote.response

data class SelfCheckRequest(
    var newRegister: Int,           // 1 if new user, 0 if not
    var transactionAmount: Double,  // The average amount spent on gambling
    var userTotalCashout: Double,  // Total cash withdrawn by the user
    var companyTotalCashout: Double, // Total cash withdrawn by the company
    var userTotalBalance: Double   // The user's balance (negative if loss)
)
