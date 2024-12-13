package com.capstone.free.education.view.selfcheck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.free.education.R
import com.capstone.free.education.data.pref.SelfCheckResponse
import com.capstone.free.education.data.remote.response.AskPredictRequest
import com.capstone.free.education.data.remote.response.AskPredictResponse
import com.capstone.free.education.data.remote.response.PredictionData
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.databinding.FragmentSelfCheckBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelfCheckFragment : Fragment() {

    private lateinit var spNewRegister: Spinner
    private lateinit var etTransactionAmount: EditText
    private lateinit var etUserCashout: EditText
    private lateinit var etCompanyCashout: EditText
    private lateinit var etUserBalance: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvResult: TextView
    private lateinit var tvPredictionLink: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_self_check, container, false)

        // Inisialisasi view
        spNewRegister = view.findViewById(R.id.spNewRegister)
        etTransactionAmount = view.findViewById(R.id.etTransactionAmount)
        etUserCashout = view.findViewById(R.id.etUserCashout)
        etCompanyCashout = view.findViewById(R.id.etCompanyCashout)
        etUserBalance = view.findViewById(R.id.etUserBalance)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        tvResult = view.findViewById(R.id.tvResult)
        tvPredictionLink = view.findViewById(R.id.tvPredictionLink)

        // Set tombol submit
        btnSubmit.setOnClickListener { submitSelfCheck() }

        return view
    }

    private fun submitSelfCheck() {
        val newRegister = if (spNewRegister.selectedItem.toString() == "YA") 1 else 0
        val transactionAmount = etTransactionAmount.text.toString().toIntOrNull()
        val userCashout = etUserCashout.text.toString().toIntOrNull()
        val companyCashout = etCompanyCashout.text.toString().toIntOrNull()
        val userBalance = etUserBalance.text.toString().toIntOrNull()

        if (transactionAmount == null || userCashout == null || companyCashout == null || userBalance == null) {
            Toast.makeText(context, "Semua field harus diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val request = AskPredictRequest(
            newRegister = newRegister,
            transaction_amount = transactionAmount,
            user_total_cashout = userCashout,
            company_total_cashout = companyCashout,
            user_total_balance = userBalance
        )

        ApiConfig.getApiService().askPredict(request).enqueue(object : Callback<AskPredictResponse> {
            override fun onResponse(
                call: Call<AskPredictResponse>,
                response: Response<AskPredictResponse>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()?.data
                    tvResult.text = "Prediksi: ${result?.predicted_addiction}\nRekomendasi: ${result?.kategori}"
                    tvPredictionLink.visibility = View.VISIBLE
                    tvPredictionLink.text = result?.link
                    tvPredictionLink.setOnClickListener {
                        // Bisa buka link di browser atau menggunakan intent
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result?.link))
                        startActivity(intent)
                    }
                } else {
                    tvResult.text = "Gagal mendapatkan hasil."
                }
            }

            override fun onFailure(call: Call<AskPredictResponse>, t: Throwable) {
                tvResult.text = "Error: ${t.message}"
            }
        })
    }
}



