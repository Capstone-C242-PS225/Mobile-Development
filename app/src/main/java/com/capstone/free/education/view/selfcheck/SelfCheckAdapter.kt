package com.capstone.free.education.view.selfcheck

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.capstone.free.education.R
import com.google.android.material.textfield.TextInputEditText

class SelfCheckAdapter(
    private val questions: List<String>,
    private val onAnswerSubmit: (String, Int) -> Unit
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

        // Tambahkan TextWatcher untuk menangkap perubahan teks
        holder.answerEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val answer = s.toString()
                onAnswerSubmit(answer, position)  // Menyimpan jawaban berdasarkan posisi
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun getItemCount(): Int = questions.size
}
