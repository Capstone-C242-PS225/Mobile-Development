package com.capstone.free.education.data.event.retrofit


import com.capstone.free.education.data.response.DetailEvent
import com.capstone.free.education.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int,
    ): Call<EventResponse>

    @GET("events")
    fun searchEvents(@Query("active") active: Int, @Query("q") query: String): Call<EventResponse>

    @GET("events")

    suspend fun getEvent(
        @Query("active") active: Int
    ) : EventResponse

    @GET("events/{id}")

    suspend fun getEventById(
        @Path("id") id : Int
    ): DetailEvent

    @GET("events")
    suspend fun getEventByKeyword(
        @Query("active") active: Int,
        @Query("q") q : String
    ): EventResponse

    @GET("events")
    suspend fun getUpdatedEvent(
        @Query("active") active: Int,
        @Query("limit") limit : Int
    ): EventResponse
}

