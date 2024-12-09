package com.capstone.free.education.data.event.room

import androidx.room.*
import com.capstone.free.education.data.event.entity.EventEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface EventDao {

    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun getFavoriteEventById(id : Int): EventEntity?

    @Query("DELETE FROM favorite_table WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(eventEntity : EventEntity)

    @Update
    suspend fun updateEvents(eventEntity : EventEntity)
}