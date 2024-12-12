package com.capstone.free.education.data.pref

data class SelfCheckResponse(
    var newRegister: Boolean? = null,
    var transactionAmount: Double = 0.0,
    var userTotalCashout: Double = 0.0,
    var companyTotalCashout: Double = 0.0,
    var profitOrLoss: String? = null
) {

    // Method to check if all required fields are filled
    fun isComplete(): Boolean {
        // Check if all required fields are non-null and non-empty (adjust logic based on actual needs)
        return newRegister != null &&
                transactionAmount > 0 &&
                userTotalCashout > 0 &&
                companyTotalCashout > 0 &&
                !profitOrLoss.isNullOrEmpty()
    }
}
