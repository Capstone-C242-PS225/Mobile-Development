package com.capstone.free.education.view.splash

import androidx.lifecycle.ViewModel
import com.capstone.free.education.data.pref.UserModel
import com.capstone.free.education.data.remote.repo.UserRepository
import kotlinx.coroutines.flow.Flow


class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    // Mengembalikan LiveData yang berisi token pengguna
    fun getUserToken(): Flow<UserModel> {
        return repository.getSession()
    }
}
