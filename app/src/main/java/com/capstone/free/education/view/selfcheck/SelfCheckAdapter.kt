package com.capstone.free.education.view.selfcheck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.R
import com.capstone.free.education.data.pref.Question
import com.google.android.material.textfield.TextInputEditText

class SelfCheckAdapter (
    private val questions: List<String>,
    private val onAnswerSubmit: (String) -> Unit
) : RecyclerView.Adapter<SelfCheckAdapter.SelfCheckViewHolder>() {

    class SelfCheckViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = view.findViewById(R.id.tv_question)
        val answerEditText: TextInputEditText = view.findViewById(R.id.tiet_inputTextEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelfCheckViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question_answer, parent, false)
        return SelfCheckViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelfCheckViewHolder, position: Int) {
        val question = questions[position]
        holder.questionTextView.text = question

        holder.answerEditText.setOnFocusChangeListener { _, _ ->
            val answer = holder.answerEditText.text.toString()
            onAnswerSubmit(answer)
        }
    }

    override fun getItemCount(): Int = questions.size
}