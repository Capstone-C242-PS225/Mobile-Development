package com.capstone.free.education.data.remote.response

import com.google.gson.annotations.SerializedName

data class AskPredictRequest(
	val newRegister: Int,
	val transaction_amount: Int,
	val user_total_cashout: Int,
	val company_total_cashout: Int,
	val user_total_balance: Int
)

data class AskPredictResponse(
	val status: String,
	val message: String,
	val data: PredictionData
)

data class PredictionData(
	val id: String,
	val createdAt: String,
	val predicted_addiction: String,
	val prediction_probabilities: List<List<Double>>,
	val kategori: String,
	val link: String
)
