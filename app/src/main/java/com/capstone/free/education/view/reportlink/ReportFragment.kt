package com.capstone.free.education.view.reportlink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.capstone.free.education.R
import com.capstone.free.education.data.remote.response.ReportRequest
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch


class ReportFragment : Fragment(R.layout.fragment_report) {

    private val apiService = ApiConfig.getApiService() // Inisialisasi ApiService dengan Retrofit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referensi ke EditText dan Button
        val editText = view.findViewById<EditText>(R.id.etInput)
        val sendButton = view.findViewById<Button>(R.id.btnSend)

        // Aksi saat tombol "Kirim" diklik
        sendButton.setOnClickListener {
            val inputText = editText.text.toString()

            if (inputText.isNotBlank()) {
                // Simulasi pengiriman data ke server
                sendDataToServer(inputText)

                // Hapus teks setelah berhasil
                editText.text.clear()

                // Beri notifikasi ke pengguna
                Toast.makeText(requireContext(), "Link berhasil dikirim!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Masukkan link terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendDataToServer(input: String) {
        lifecycleScope.launch {
            try {
                val response = apiService.reportLink(ReportRequest(input))
                if (response.isSuccessful) {
                    // Berhasil
                    Toast.makeText(requireContext(), "Link berhasil dilaporkan!", Toast.LENGTH_SHORT).show()
                } else {
                    // Gagal
                    Toast.makeText(requireContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                // Tangani error
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
