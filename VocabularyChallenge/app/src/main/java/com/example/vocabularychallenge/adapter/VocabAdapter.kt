package com.example.vocabularychallenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularychallenge.databinding.ItemVocabBinding
import com.example.vocabularychallenge.model.Vocab

class VocabAdapter(private val items: List<Vocab>): RecyclerView.Adapter<VocabAdapter.VH>() {
    inner class VH(val binding: ItemVocabBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemVocabBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val v = items[position]
        holder.binding.tvEng.text = v.english
        holder.binding.tvCh.text = v.chinese
    }
    override fun getItemCount() = items.size
}
