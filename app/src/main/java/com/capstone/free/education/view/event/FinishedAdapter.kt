package com.capstone.free.education.view.event


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.free.education.R
import com.capstone.free.education.data.response.ListEventsItem
import com.capstone.free.education.databinding.ItemEventBinding


class FinishedAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, FinishedAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    class MyViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvTitle.text = event.name
            binding.tvSubtitle.text = event.summary

            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .placeholder(R.drawable.loading_placeholder)
                .error(R.drawable.error_image)
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
