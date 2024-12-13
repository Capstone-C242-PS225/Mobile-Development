package com.capstone.free.education.view.materi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.free.education.databinding.FragmentMateriBinding
import com.capstone.free.education.data.remote.retrofit.ApiConfig
import com.capstone.free.education.data.remote.response.MateriResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MateriFragment : Fragment() {

    private lateinit var binding: FragmentMateriBinding
    private lateinit var materiAdapter: MateriAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMateriBinding.inflate(inflater, container, false)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        materiAdapter = MateriAdapter(emptyList())
        binding.recyclerView.adapter = materiAdapter

        // Fetch data from API
        fetchMateri()

        return binding.root
    }

    private fun fetchMateri() {
        val call = ApiConfig.getApiService().getMateri()
        call.enqueue(object : Callback<MateriResponse> {
            override fun onResponse(call: Call<MateriResponse>, response: Response<MateriResponse>) {
                if (response.isSuccessful) {
                    val materiResponse = response.body()
                    if (materiResponse != null && !materiResponse.error) {
                        materiAdapter = MateriAdapter(materiResponse.data)
                        binding.recyclerView.adapter = materiAdapter
                    } else {
                        Toast.makeText(requireContext(), "Failed to fetch materi", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MateriResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
