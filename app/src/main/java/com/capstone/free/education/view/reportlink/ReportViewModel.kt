package com.capstone.free.education.view.reportlink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {

    private val _sendStatus = MutableLiveData<String>()
    val sendStatus: LiveData<String> get() = _sendStatus

    fun sendDataToServer(input: String) {
        viewModelScope.launch {
            try {
                println("Mengirim data ke server: $input")

                _sendStatus.value = "Link berhasil dikirim!"
            } catch (e: Exception) {
                _sendStatus.value = "Gagal mengirim link: ${e.message}"
            }
        }
    }
}