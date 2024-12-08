package com.capstone.free.education.view.splash

import com.capstone.free.education.data.UserRepository
import androidx.lifecycle.ViewModel
import com.capstone.free.education.data.pref.UserModel
import kotlinx.coroutines.flow.Flow


class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    // Mengembalikan LiveData yang berisi token pengguna
    fun getUserToken(): Flow<UserModel> {
        return repository.getSession()
    }
}
