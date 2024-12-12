package com.capstone.free.education.data.remote.retrofit

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Endpoint POST /askPredict
    @POST("askPredict")
    suspend fun askPredict(
        @Body selfCheckRequest: SelfCheckRequest
    ): Response<SelfCheckResponse>

    @POST("submit-selfcheck")
    fun submitSelfCheckResponse(@Body response: SelfCheckRequest): Call<ApiResponse>
}

