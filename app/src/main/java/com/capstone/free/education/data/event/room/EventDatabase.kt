package com.capstone.free.education.data.event.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.capstone.free.education.data.event.entity.EventEntity


@Database(entities = [EventEntity::class], version = 2, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    abstract fun eventDao() : EventDao

    companion object {
        @Volatile
        private var INSTANCE : EventDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context) : EventDatabase {
            if(INSTANCE == null) {
                synchronized(EventDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, EventDatabase::class.java, "dicoding_event_database").build()
                }
            }
            return INSTANCE as EventDatabase
        }
    }
}