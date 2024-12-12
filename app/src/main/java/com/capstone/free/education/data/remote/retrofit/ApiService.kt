package com.capstone.free.education.data.remote.retrofit


import com.capstone.free.education.data.remote.response.LoginRequest
import com.capstone.free.education.data.remote.response.LoginResponse
import com.capstone.free.education.data.remote.response.RegisterRequest
import com.capstone.free.education.data.remote.response.RegisterResponse
import com.capstone.free.education.data.response.DetailEvent
import com.capstone.free.education.data.response.EventResponse
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

}

