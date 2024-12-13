package com.capstone.free.education.view.selfcheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.free.education.data.pref.SelfCheckResponse
import com.capstone.free.education.data.remote.response.PredictionData
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.databinding.FragmentSelfCheckBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelfCheckFragment : Fragment() {

    private lateinit var binding: FragmentSelfCheckBinding
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
    ): View {
        binding = FragmentSelfCheckBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupSendButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = SelfCheckAdapter(questions) { answer, position ->
            updateUserResponse(answer, position)
        }
        binding.rvChatHistory.layoutManager = LinearLayoutManager(context)
        binding.rvChatHistory.adapter = adapter
    }

    private fun updateUserResponse(answer: String, position: Int) {
        when (position) {
            0 -> userResponse.newRegister = answer.equals("ya", ignoreCase = true)
            1 -> userResponse.transactionAmount = answer.toDoubleOrNull() ?: 0.0
            2 -> userResponse.userTotalCashout = answer.toDoubleOrNull() ?: 0.0
            3 -> userResponse.companyTotalCashout = answer.toDoubleOrNull() ?: 0.0
            4 -> userResponse.profitOrLoss = answer
        }
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener {
            preparePayload()
            if (userResponse.isComplete()) {
                sendToApi(userResponse)
            } else {
                Toast.makeText(context, "Harap jawab semua pertanyaan!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun SelfCheckResponse.isComplete(): Boolean {
        return newRegister != null &&
                transactionAmount > 0.0 &&
                userTotalCashout > 0.0 &&
                companyTotalCashout > 0.0 &&
                !profitOrLoss.isNullOrEmpty()
    }

    private fun preparePayload() {
        if (userResponse.newRegister == null) userResponse.newRegister = false
        if (userResponse.transactionAmount == 0.0) userResponse.transactionAmount = 1.0
        if (userResponse.userTotalCashout == 0.0) userResponse.userTotalCashout = 1.0
        if (userResponse.companyTotalCashout == 0.0) userResponse.companyTotalCashout = 1.0
        if (userResponse.profitOrLoss.isNullOrEmpty()) userResponse.profitOrLoss = "tidak ada"
    }

    private fun sendToApi(response: SelfCheckResponse) {
        val jsonResponse = Gson().toJson(response)
        Log.d("SelfCheckFragment", "JSON Request: $jsonResponse")

        val apiService = ApiConfig.getApiService()
        val call = apiService.sendSelfCheckData(jsonResponse)

        call.enqueue(object : Callback<PredictionData> {
            override fun onResponse(call: Call<PredictionData>, response: Response<PredictionData>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        showResult(it)
                    } ?: run {
                        Log.e("SelfCheckFragment", "Response body is null")
                        Toast.makeText(context, "Gagal mengirim data!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    logAndShowError(response)
                }
            }

            override fun onFailure(call: Call<PredictionData>, t: Throwable) {
                Log.e("SelfCheckFragment", "Network Failure: ${t.message}", t)
                Toast.makeText(context, "Terjadi kesalahan jaringan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logAndShowError(response: Response<*>) {
        val errorBody = response.errorBody()?.string()
        Log.e("SelfCheckFragment", "Error Code: ${response.code()}, Error Body: $errorBody")
        Toast.makeText(context, "Gagal mengirim data! Error Code: ${response.code()}", Toast.LENGTH_SHORT).show()
    }

    private fun showResult(result: PredictionData) {
        val prediction = result.data?.predictedAddiction
        Toast.makeText(context, "Hasil Analisis: $prediction", Toast.LENGTH_LONG).show()
    }
}
