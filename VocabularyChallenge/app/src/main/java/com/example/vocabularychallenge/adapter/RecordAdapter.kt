package com.example.vocabularychallenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularychallenge.databinding.ItemRecordBinding
import com.example.vocabularychallenge.model.UserRecord

class RecordAdapter(private val items: List<UserRecord>): RecyclerView.Adapter<RecordAdapter.VH>() {
    inner class VH(val b: ItemRecordBinding): RecyclerView.ViewHolder(b.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        holder.b.tvDate.text = r.date
        holder.b.tvUnit.text = "單元: ${r.unit}"
        holder.b.tvScore.text = "分數: ${r.score}"
        holder.b.tvTime.text = "用時: ${r.timeSpent} 秒"
    }
    override fun getItemCount() = items.size
}
