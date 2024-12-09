package com.capstone.free.education.data.event

import android.util.Log
import com.capstone.free.education.data.event.entity.EventEntity
import com.capstone.free.education.data.event.retrofit.ApiService
import com.capstone.free.education.data.event.room.EventDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun getAllFavoriteEvents(): Flow<List<EventEntity>> = eventDao.getAllFavoriteEvents()

    suspend fun insertFavoriteEvent(event: EventEntity) {
        return withContext(Dispatchers.IO) {
            Log.d("EventRepository", "Inserting event: ${event.name}")
            eventDao.insertEvents(event)
            Log.d("EventRepository", "Event inserted: ${event.id}")
        }
    }

    suspend fun deleteEvent(id: Int) {
        return withContext(Dispatchers.IO) {
            eventDao.deleteFavoriteEvent(id)
        }
    }

    suspend fun getEventById(id: Int): EventEntity? {
        return withContext(Dispatchers.IO) {
            eventDao.getFavoriteEventById(id)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: EventRepository(apiService, eventDao, appExecutors)
        }.also { INSTANCE = it }
    }
}
