package com.capstone.free.education.view.reportlink

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.capstone.free.education.R
import com.capstone.free.education.data.remote.response.ReportRequest
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class ReportFragment : Fragment(R.layout.fragment_report) {

    private val reportViewModel: ReportViewModel by viewModels()
    private val apiService = ApiConfig.getApiService() // Inisialisasi ApiService dengan Retrofit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referensi ke EditText dan Button
        val editText = view.findViewById<EditText>(R.id.etInput)
        val sendButton = view.findViewById<Button>(R.id.btnSend)

        // Observasi status pengiriman data
        reportViewModel.sendStatus.observe(viewLifecycleOwner, Observer { status ->
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
        })

        // Aksi saat tombol "Kirim" diklik
        sendButton.setOnClickListener {
            val inputText = editText.text.toString()

            if (inputText.isNotBlank()) {
                // Kirim data ke ViewModel
                reportViewModel.sendDataToServer(inputText)

                // Hapus teks setelah mengirim
                editText.text.clear()
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
