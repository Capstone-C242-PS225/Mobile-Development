package com.capstone.free.education.data.event


import android.content.Context
import com.capstone.free.education.data.event.retrofit.ApiConfig
import com.capstone.free.education.data.event.room.EventDatabase



object Injection {
    fun provideRepository(context: Context) : EventRepository {
        val apiService = ApiConfig.getApiService()
        val db = EventDatabase.getDatabase(context)
        val dao = db.eventDao()
        val executor = AppExecutors()
        return EventRepository.getInstance(apiService, dao, executor)
    }
}