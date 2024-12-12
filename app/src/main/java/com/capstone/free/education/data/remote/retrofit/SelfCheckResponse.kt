package com.capstone.free.education.data.remote.retrofit

import com.google.gson.annotations.SerializedName

data class SelfCheckResponse(
    @SerializedName("prediction") val prediction: String, // Prediksi hasil analisis
    @SerializedName("message") val message: String, // Pesan terkait hasil analisis
    @SerializedName("status") val status: Int // Status dari respons API (misalnya, kode status HTTP atau status aplikasi)
)
