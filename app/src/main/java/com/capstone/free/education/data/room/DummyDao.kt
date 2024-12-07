package com.capstone.free.education.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.capstone.free.education.data.entity.DummyEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DummyDao {

    @Query("SELECT * FROM favorite_table")
    fun getAllFavoriteEvents(): Flow<List<DummyEntity>>

    @Query("SELECT * FROM favorite_table WHERE id = :id")
    fun getFavoriteEventById(id : Int): DummyEntity?

    @Query("DELETE FROM favorite_table WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(eventEntity : DummyEntity)

    @Update
    suspend fun updateEvents(eventEntity : DummyEntity)
}