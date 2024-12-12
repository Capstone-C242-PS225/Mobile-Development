package com.capstone.free.education.view.selfcheck

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.data.remote.response.SelfCheckRequest
import com.capstone.free.education.data.remote.response.SelfCheckResponse
import com.capstone.free.education.databinding.FragmentSelfcheckBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class selfcheckFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SelfCheckAdapter
    private var questions: List<String> = listOf()  // List of questions fetched from the API
    private var userResponse = SelfCheckRequest(0, 0.0, 0.0, 0.0, 0.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelfcheckBinding.inflate(inflater, container, false)

        recyclerView = binding.rvChatHistory
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Call the API to fetch questions
        fetchQuestionsFromApi()

        binding.btnSend.setOnClickListener {
            if (userResponse.isComplete()) {
                sendToApi(userResponse)
            } else {
                Toast.makeText(requireContext(), "Harap jawab semua pertanyaan!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun fetchQuestionsFromApi() {
        val call = ApiConfig.getApiService().askPredict(SelfCheckRequest(0, 0.0, 0.0, 0.0, 0.0))

        call.enqueue(object : Callback<SelfCheckResponse> {
            override fun onResponse(call: Call<SelfCheckResponse>, response: Response<SelfCheckResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        questions = result.questions
                        setupRecyclerView()
                    } else {
                        Toast.makeText(requireContext(), "No questions available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load questions", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SelfCheckResponse>, t: Throwable) {
                Log.e("SelfCheckFragment", "API Error: ${t.message}")
                Toast.makeText(requireContext(), "Error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerView() {
        if (questions.isNotEmpty()) {
            adapter = SelfCheckAdapter(questions) { answer ->
                when (questions.indexOf(answer)) {
                    0 -> userResponse.newRegister = if (answer == "ya") 1 else 0
                    1 -> userResponse.transactionAmount = answer.toDoubleOrNull() ?: 0.0
                    2 -> userResponse.userTotalCashout = answer.toDoubleOrNull() ?: 0.0
                    3 -> userResponse.companyTotalCashout = answer.toDoubleOrNull() ?: 0.0
                    4 -> userResponse.userTotalBalance = if (answer == "untung") 1.0 else -1.0
                }
            }
            recyclerView.adapter = adapter
        } else {
            Toast.makeText(requireContext(), "No questions available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun SelfCheckRequest.isComplete(): Boolean {
        return newRegister != 0 && transactionAmount != 0.0 && userTotalCashout != 0.0 &&
                companyTotalCashout != 0.0 && userTotalBalance != 0.0
    }

    private fun sendToApi(response: SelfCheckRequest) {
        val call = ApiConfig.getApiService().askPredict(response)

        call.enqueue(object : Callback<SelfCheckResponse> {
            override fun onResponse(call: Call<SelfCheckResponse>, response: Response<SelfCheckResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    showResult(result)
                } else {
                    Toast.makeText(requireContext(), "Failed to send data!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SelfCheckResponse>, t: Throwable) {
                Log.e("SelfCheckFragment", "API call failed: ${t.message}")
                t.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showResult(result: SelfCheckResponse?) {
        if (result != null) {
            Toast.makeText(requireContext(), "Hasil Analisis: ${result.prediction}", Toast.LENGTH_LONG).show()
        }
    }
}
