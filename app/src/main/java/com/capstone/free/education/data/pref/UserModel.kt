package com.capstone.free.education.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
) {
    fun isNullOrEmpty(): Boolean {
        // Memeriksa apakah email dan token kosong atau null, atau jika isLogin false
        return email.isEmpty() || token.isEmpty() || !isLogin
    }
}
