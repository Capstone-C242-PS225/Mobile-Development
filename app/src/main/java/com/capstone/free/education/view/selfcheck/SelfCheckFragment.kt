package com.capstone.free.education.view.selfcheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.data.remote.retrofit.ApiResponse
import com.capstone.free.education.data.remote.retrofit.ApiService
import com.capstone.free.education.data.remote.retrofit.SelfCheckRequest
import com.capstone.free.education.data.remote.retrofit.SelfCheckResponse
import com.capstone.free.education.databinding.FragmentSelfCheckBinding
import com.capstone.free.education.view.selfcheck.SelfCheckAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelfCheckFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelfCheckAdapter
    private val questions = mutableListOf<String>() // List untuk menyimpan pertanyaan yang diambil dari API

    private var userResponse = SelfCheckRequest(
        newRegister = 0,
        transaction_amount = 0,
        user_total_cashout = 0,
        company_total_cashout = 0,
        user_total_balance = 0
    )

    private var apiService: ApiService? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelfCheckBinding.inflate(inflater, container, false)

        recyclerView = binding.rvChatHistory
        adapter = SelfCheckAdapter(questions) { answer ->
            // Menyimpan jawaban ke dalam userResponse berdasarkan indeks pertanyaan
            when (questions.indexOf(answer)) {
                0 -> userResponse.newRegister = if (answer == "ya") 1 else 0
                1 -> userResponse.transaction_amount = answer.toIntOrNull() ?: 0
                2 -> userResponse.user_total_cashout = answer.toIntOrNull() ?: 0
                3 -> userResponse.company_total_cashout = answer.toIntOrNull() ?: 0
                4 -> userResponse.user_total_balance = answer.toIntOrNull() ?: 0
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Mengambil pertanyaan atau data dari API untuk analisis
        fetchPrediction()

        return binding.root
    }

    // Menggunakan coroutine untuk memanggil askPredict
    // Menggunakan coroutine untuk memanggil askPredict
    private fun fetchPrediction() {
        apiService = ApiConfig.getApiService()

        // Menjalankan dalam coroutine
        lifecycleScope.launch {
            try {
                // Panggilan API menggunakan `askPredict` dengan objek SelfCheckRequest
                val call: Response<SelfCheckResponse> = apiService?.askPredict(userResponse) ?: return@launch

                // Menangani respons API dengan `enqueue`
                call.enqueue(object : Callback<SelfCheckResponse> {
                    override fun onResponse(call: Call<SelfCheckResponse>, response: Response<SelfCheckResponse>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result != null) {
                                // Tampilkan hasil analisis prediksi dari API
                                showResult(result)
                            } else {
                                Toast.makeText(context, "Gagal memuat hasil analisis", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Gagal memuat hasil analisis", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SelfCheckResponse>, t: Throwable) {
                        Toast.makeText(context, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            } catch (e: Exception) {
                Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()  // Mencetak stack trace untuk debug
            }
        }
    }


    // Mengecek apakah semua pertanyaan sudah dijawab sebelum dikirim ke API
    private fun SelfCheckRequest.isComplete(): Boolean {
        return newRegister != 0 && transaction_amount != 0 && user_total_cashout != 0 &&
                company_total_cashout != 0 && user_total_balance != 0
    }

    // Menangani pengiriman data dan validasi sebelum ke API
    private fun submitAnswers() {
        if (userResponse.isComplete()) {
            sendToApi(userResponse)
        } else {
            Toast.makeText(context, "Harap jawab semua pertanyaan!", Toast.LENGTH_SHORT).show()
        }
    }

    // Mengirimkan data ke API untuk analisis lebih lanjut
    private fun sendToApi(response: SelfCheckRequest) {
        lifecycleScope.launch {
            try {
                val call: Call<ApiResponse> = apiService?.submitSelfCheckResponse(response) ?: return@launch

                // Menangani respons API dengan `enqueue`
                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result != null) {
                                // Tampilkan hasil analisis dari API
                                showResult(result)
                            } else {
                                Toast.makeText(context, "Gagal mengirim data!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Gagal mengirim data!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Toast.makeText(context, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            } catch (e: Exception) {
                Toast.makeText(context, "Terjadi kesalahan: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()  // Mencetak stack trace untuk debug
            }
        }
    }

    // Menampilkan hasil dari analisis API
    private fun showResult(result: Any?) {
        if (result is SelfCheckResponse) {
            Toast.makeText(context, "Hasil Analisis: ${result.prediction}", Toast.LENGTH_LONG).show()
        } else if (result is ApiResponse) {
            Toast.makeText(context, "Hasil Analisis: ${result.analysis}", Toast.LENGTH_LONG).show()
        }
    }
}
