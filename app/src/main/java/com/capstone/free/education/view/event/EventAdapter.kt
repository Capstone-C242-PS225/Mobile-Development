package com.capstone.free.education.view.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.free.education.data.response.ListEventsItem
import com.capstone.free.education.databinding.ItemEventBinding


abstract class EventAdapter : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvTitle.text = event.name
            binding.tvSubtitle.text = event.summary
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgImage)


            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id // Asumsikan setiap event punya ID unik
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
