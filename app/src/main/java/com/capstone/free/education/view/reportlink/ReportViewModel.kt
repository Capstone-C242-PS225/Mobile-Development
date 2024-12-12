package com.capstone.free.education.view.reportlink

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {

    // LiveData untuk status pengiriman data
    private val _sendStatus = MutableLiveData<String>()
    val sendStatus: LiveData<String> get() = _sendStatus

    // Fungsi untuk mengirim data ke server
    fun sendDataToServer(input: String) {
        viewModelScope.launch {
            try {
                // Simulasi pengiriman data ke server
                // Ganti dengan logika pengiriman data sesungguhnya
                println("Mengirim data ke server: $input")

                // Jika berhasil
                _sendStatus.value = "Link berhasil dikirim!"
            } catch (e: Exception) {
                // Jika terjadi kesalahan
                _sendStatus.value = "Gagal mengirim link: ${e.message}"
            }
        }
    }
}