package com.capstone.free.education.view.konsultasi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.free.education.R
import com.capstone.free.education.data.pref.konsultasi
import com.capstone.free.education.databinding.ItemKonsultasiBinding

class konsultasiAdapter(private val konsultasiList: List<konsultasi>) :
    RecyclerView.Adapter<konsultasiAdapter.KonsultasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KonsultasiViewHolder {
        val binding = ItemKonsultasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KonsultasiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KonsultasiViewHolder, position: Int) {
        val item = konsultasiList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = konsultasiList.size

    class KonsultasiViewHolder(private val binding: ItemKonsultasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(konsultasi: konsultasi) {
            Glide.with(binding.imageKonsultasi.context)
                .load(konsultasi.imageUrl)
                .into(binding.imageKonsultasi)

            binding.namaKonsultasi.text = konsultasi.name
            binding.deskKonsultasi.text = konsultasi.description
        }
    }
}

