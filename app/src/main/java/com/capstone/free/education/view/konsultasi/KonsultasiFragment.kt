package com.capstone.free.education.view.konsultasi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.R
import com.capstone.free.education.data.pref.konsultasi


class KonsultasiFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: konsultasiAdapter
    private lateinit var konsultasiList: MutableList<konsultasi>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_konsultasi
        return inflater.inflate(R.layout.fragment_konsultasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inisialisasi RecyclerView dan set LayoutManager
        recyclerView = view.findViewById(R.id.rvKonsultasi)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        konsultasiList = mutableListOf()

        // Menambahkan data dummy
        konsultasiList.add(konsultasi(1, "Leanne Graham", "https://www.example.com/image1.jpg", "Psychologist with experience in anxiety and depression"))
        konsultasiList.add(konsultasi(2, "Ervin Howell", "https://www.example.com/image2.jpg", "Psychologist with expertise in relationship counseling"))

        // Set Adapter
        adapter = konsultasiAdapter(konsultasiList)
        recyclerView.adapter = adapter
    }
}
