package com.capstone.free.education.view.selfcheck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.R
import com.google.android.material.textfield.TextInputEditText

class SelfCheckAdapter(
    private val questions: List<String>,
    private val onAnswerSelected: (String) -> Unit
) : RecyclerView.Adapter<SelfCheckAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size

    inner class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(android.R.id.text1)

        fun bind(question: String) {
            textView.text = question
            itemView.setOnClickListener {
                onAnswerSelected(question)  // Trigger answer selection
            }
        }
    }
}
