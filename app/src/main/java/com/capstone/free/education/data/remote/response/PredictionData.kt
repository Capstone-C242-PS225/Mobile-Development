package com.capstone.free.education.data.remote.response

import com.google.gson.annotations.SerializedName

data class PredictionData(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("predicted_addiction")
	val predictedAddiction: String? = null,

	@field:SerializedName("prediction_probabilities")
	val predictionProbabilities: List<List<Any?>?>? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("kategori")
	val kategori: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
