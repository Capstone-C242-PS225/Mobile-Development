package com.capstone.free.education.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.free.education.data.remote.repo.UserRepository
import com.capstone.free.education.di.Injection
import com.capstone.free.education.view.login.LoginViewModel
import com.capstone.free.education.view.main.MainViewModel
import com.capstone.free.education.view.profile.ProfileFragment
import com.capstone.free.education.view.setting.SettingPreferences
import com.capstone.free.education.view.setting.SettingViewModel

class ViewModelFactory(private val repository: UserRepository, private val setting: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(setting) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context, setting: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context), setting)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
