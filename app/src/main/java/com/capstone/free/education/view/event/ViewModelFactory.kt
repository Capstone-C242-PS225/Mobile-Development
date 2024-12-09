package com.capstone.free.education.view.event

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.free.education.data.event.EventRepository
import com.capstone.free.education.data.event.Injection
import com.capstone.free.education.data.pref.dataStore
import com.capstone.free.education.view.setting.SettingPreferences


class ViewModelFactory(
    private val preference : SettingPreferences,
    private val eventRepository: EventRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EventViewModel::class.java) -> {
                EventViewModel(preference, eventRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class data : ${modelClass.name} please create new ViewModel at factory")
        }
    }

    companion object {
        @Volatile
        private var instance : ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                val eventRepository = Injection.provideRepository(context)
                val preference = SettingPreferences.getInstance(context.dataStore)
                instance ?: ViewModelFactory(preference, eventRepository)
            }.also { instance =  it }
    }
}