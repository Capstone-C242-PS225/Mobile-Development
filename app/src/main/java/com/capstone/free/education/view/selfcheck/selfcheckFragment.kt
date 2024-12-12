package com.capstone.free.education.view.selfcheck

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.data.pref.SelfCheckResponse
import com.capstone.free.education.data.remote.response.PredictionData
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.databinding.FragmentSelfcheckBinding
import com.google.gson.Gson
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call


class SelfcheckFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: selfcheckAdapter
    private val questions = listOf(
        "Apakah anda merupakan pengguna baru judi online?",
        "Berapa rata-rata uang yang telah anda gunakan untuk bermain judi online?",
        "Berapa jumlah uang yang sudah anda keluarkan dari judi online?",
        "Berapa uang yang anda dapat dari bermain judi online?",
        "Apakah anda mengalami keuntungan atau kerugian selama bermain judi online?"
    )

    private var userResponse = SelfCheckResponse()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelfcheckBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        recyclerView = binding.rvChatHistory
        adapter = selfcheckAdapter(questions) { answer, position ->
            // Simpan jawaban berdasarkan urutan pertanyaan
            when (position) {
                0 -> userResponse.newRegister = answer == "ya"
                1 -> userResponse.transactionAmount = answer.toDoubleOrNull() ?: 0.0
                2 -> userResponse.userTotalCashout = answer.toDoubleOrNull() ?: 0.0
                3 -> userResponse.companyTotalCashout = answer.toDoubleOrNull() ?: 0.0
                4 -> userResponse.profitOrLoss = answer
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Tombol Kirim Jawaban
        binding.btnSend.setOnClickListener {
            if (userResponse.isComplete()) {
                sendToApi(userResponse)
            } else {
                Toast.makeText(context, "Harap jawab semua pertanyaan!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // Mengecek apakah semua pertanyaan sudah dijawab
    private fun SelfCheckResponse.isComplete(): Boolean {
        return newRegister != null && transactionAmount != 0.0 && userTotalCashout != 0.0 &&
                companyTotalCashout != 0.0 && profitOrLoss.isNotEmpty()
    }

    // Mengirim data ke API
    private fun sendToApi(response: SelfCheckResponse) {
        val jsonResponse = Gson().toJson(response)
        val apiService = ApiConfig.getApiService()
        val call = apiService.sendSelfCheckData(jsonResponse)

        call.enqueue(object : Callback<PredictionData> {
            override fun onResponse(call: retrofit2.Call<PredictionData>, response: Response<PredictionData>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    showResult(result)
                } else {
                    Toast.makeText(context, "Gagal mengirim data!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<PredictionData>, t: Throwable) {
                Toast.makeText(context, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Menampilkan hasil analisis dari API
    private fun showResult(result: PredictionData?) {
        result?.let {
            val prediction = it.data?.predictedAddiction
            Toast.makeText(context, "Hasil Analisis: $prediction", Toast.LENGTH_LONG).show()
        }
    }
}
