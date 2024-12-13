package com.capstone.free.education.view.materi

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.data.remote.response.MateriData
import com.capstone.free.education.databinding.ItemMateriBinding
import com.bumptech.glide.Glide

class MateriAdapter(private val materiList: List<MateriData>) : RecyclerView.Adapter<MateriAdapter.MateriViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MateriViewHolder {
        val binding = ItemMateriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MateriViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MateriViewHolder, position: Int) {
        val materi = materiList[position]
        holder.bind(materi)
    }

    override fun getItemCount(): Int = materiList.size

    inner class MateriViewHolder(private val binding: ItemMateriBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(materi: MateriData) {
            binding.tvJudul.text = materi.judul
            binding.tvKategori.text = materi.kategori
            binding.tvTarget.text = materi.target

            Glide.with(binding.root.context)
                .load(materi.image)
                .into(binding.imageMateri)

            binding.btnLink.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(materi.link))
                binding.root.context.startActivity(intent)
            }
        }
    }
}
