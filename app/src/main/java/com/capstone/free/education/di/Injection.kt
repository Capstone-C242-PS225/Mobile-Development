package com.capstone.free.education.di

import android.content.Context
import com.capstone.free.education.data.remote.repo.UserRepository
import com.capstone.free.education.data.pref.UserPreference
import com.capstone.free.education.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}