package com.capstone.free.education.data.remote.response

data class SelfCheckResponse(
    val prediction: String,  // Example: "Risk" or "No Risk"
    val message: String,     // Any additional information about the result
    val status: Int,          // 0 if successful, 1 if there's an error
)

