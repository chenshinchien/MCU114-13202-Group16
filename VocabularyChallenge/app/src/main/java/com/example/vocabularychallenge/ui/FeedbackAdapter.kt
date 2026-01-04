package com.example.vocabularychallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularychallenge.databinding.ItemFeedbackRecordBinding
import com.example.vocabularychallenge.model.Feedback

class FeedbackAdapter(private var feedbackList: List<Feedback>) : RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder>() {

    inner class FeedbackViewHolder(val binding: ItemFeedbackRecordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val binding = ItemFeedbackRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeedbackViewHolder(binding)
    }

    override fun getItemCount(): Int = feedbackList.size

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        val feedback = feedbackList[position]
        holder.binding.apply {
            tvFeedbackUsername.text = "回饋者: ${feedback.username}"
            tvFeedbackDate.text = feedback.submissionDate
            tvFeedbackContent.text = feedback.feedbackContent
        }
    }

    fun updateData(newFeedbackList: List<Feedback>) {
        this.feedbackList = newFeedbackList
        notifyDataSetChanged()
    }
}
