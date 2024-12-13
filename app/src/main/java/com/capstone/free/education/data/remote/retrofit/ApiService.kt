package com.capstone.free.education.data.remote.retrofit

import com.capstone.free.education.data.remote.response.LoginRequest
import com.capstone.free.education.data.remote.response.LoginResponse
import com.capstone.free.education.data.remote.response.MateriResponse
import com.capstone.free.education.data.remote.response.PredictionData
import com.capstone.free.education.data.remote.response.RegisterRequest
import com.capstone.free.education.data.remote.response.RegisterResponse
import com.capstone.free.education.data.remote.response.SelfCheckRequest
import com.capstone.free.education.data.remote.response.SelfCheckResponse
import com.capstone.free.education.data.remote.response.ReportRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("askPredict")
    fun askPredict(
        @Body selfCheckRequest: SelfCheckRequest
    ): Call<SelfCheckResponse>  // Using Call instead of suspend for enqueue() usage

    @PUT("user/report")
    suspend fun reportLink(
        @Body reportRequest: ReportRequest
    ): Response<Void>

    @GET("getMateri")
    fun getMateri(): Call<MateriResponse>

    @Headers("Content-Type: application/json")
    @POST("askPredict")
    fun sendSelfCheckData(
        @Body requestBody: String
    ): Call<PredictionData>
}



