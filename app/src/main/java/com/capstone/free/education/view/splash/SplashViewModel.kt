package com.capstone.free.education.view.splash

import com.capstone.free.education.data.remote.repo.UserRepository
import androidx.lifecycle.ViewModel
import com.capstone.free.education.data.pref.UserModel
import kotlinx.coroutines.flow.Flow


class SplashViewModel(private val repository: UserRepository) : ViewModel() {

    fun getUserToken(): Flow<UserModel> {
        return repository.getSession()
    }
}
