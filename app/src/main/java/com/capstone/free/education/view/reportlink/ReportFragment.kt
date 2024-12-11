package com.capstone.free.education.view.reportlink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.capstone.free.education.R


class ReportFragment : Fragment(R.layout.fragment_report) {

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
        // Logika pengiriman ke server (simulasi)
        // Bisa diintegrasikan dengan retrofit, volley, atau library lain
        println("Mengirim data ke server: $input")
    }
}