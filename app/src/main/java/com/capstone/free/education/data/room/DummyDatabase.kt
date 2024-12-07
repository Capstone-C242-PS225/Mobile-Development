package com.capstone.free.education.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.free.education.data.entity.DummyEntity
import com.dicoding.mysubmission.ui.data.local.entity.EventEntity


@Database(entities = [DummyEntity::class], version = 2, exportSchema = false)
abstract class DummyDatabase : RoomDatabase() {
    abstract fun eventDao() : DummyDao

    companion object {
        @Volatile
        private var INSTANCE :DummyDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context) : DummyDatabase {
            if(INSTANCE == null) {
                synchronized(DummyDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, EventDatabase::class.java, "dicoding_event_database").build()
                }
            }
            return INSTANCE as DummyDatabase
        }
    }
}