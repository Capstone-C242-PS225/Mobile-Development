package com.capstone.free.education.view.selfcheck

import android.os.Bundle
import android.telecom.Call
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.data.remote.retrofit.ApiService
import com.capstone.free.education.data.pref.SelfCheckResponse
import com.capstone.free.education.data.remote.retrofit.ApiResponse
import com.capstone.free.education.databinding.FragmentSelfcheckBinding


class selfcheckFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SelfCheckAdapter
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

        recyclerView = binding.rvChatHistory
        adapter = SelfCheckAdapter(questions) { answer ->
            // Simpan jawaban berdasarkan urutan pertanyaan
            when (questions.indexOf(answer)) {
                0 -> userResponse.newRegister = answer == "ya"
                1 -> userResponse.transactionAmount = answer.toDoubleOrNull() ?: 0.0
                2 -> userResponse.userTotalCashout = answer.toDoubleOrNull() ?: 0.0
                3 -> userResponse.companyTotalCashout = answer.toDoubleOrNull() ?: 0.0
                4 -> {
                    userResponse.profitOrLoss = answer
                    // Analisis apakah untung atau rugi
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Tombol kirim jawaban
//        binding.btnSend.setOnClickListener {
//            // Kirim data ke API jika semua pertanyaan sudah dijawab
//            if (userResponse.isComplete()) {
//                sendToApi(userResponse)
//            } else {
//                Toast.makeText(context, "Harap jawab semua pertanyaan!", Toast.LENGTH_SHORT).show()
//            }
//        }

        return binding.root
    }

    // Mengecek apakah semua pertanyaan sudah dijawab
    private fun SelfCheckResponse.isComplete(): Boolean {
        return newRegister != null && transactionAmount != 0.0 && userTotalCashout != 0.0 &&
                companyTotalCashout != 0.0 && profitOrLoss.isNotEmpty()
    }

    // Kirim data ke API
    // Kirim data ke API
//    private fun sendToApi(response: SelfCheckResponse) {
//        // Menggunakan Gson untuk mengubah model menjadi JSON
//        val jsonResponse = Gson().toJson(response)
//
//        // Contoh kode untuk mengirim jsonResponse ke API menggunakan Retrofit
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://api.example.com/")  // Ganti dengan URL API yang sesuai
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val apiService = retrofit.create(ApiService::class.java)
//        val call = apiService.sendSelfCheckData(jsonResponse)
//
//        call.enqueue(object : Callback<ApiResponse> {
//            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                if (response.isSuccessful) {
//                    // Tanggapan sukses, tampilkan hasil analisis
//                    val result = response.body()
//                    showResult(result)
//                } else {
//                    // Tanggapan gagal
//                    Toast.makeText(context, "Gagal mengirim data!", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                // Gagal melakukan permintaan ke API
//                Toast.makeText(context, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    // Fungsi untuk menampilkan hasil analisis dari API
    private fun showResult(result: ApiResponse?) {
        // Tampilkan hasil analisis di UI (misalnya, menggunakan dialog atau fragment baru)
        if (result != null) {
            // Misalnya, tampilkan hasil analisis dalam sebuah toast
            Toast.makeText(context, "Hasil Analisis: ${result.analysis}", Toast.LENGTH_LONG).show()
        }
    }

}