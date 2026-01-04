package com.example.vocabularychallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularychallenge.databinding.ItemAdminRecordBinding // 請確認您的 package 名稱
import com.example.vocabularychallenge.model.TestResult // 請確認您的 package 名稱

class AdminRecordAdapter(private var records: List<TestResult>) : RecyclerView.Adapter<AdminRecordAdapter.RecordViewHolder>() {

    // ViewHolder 負責持有 item_admin_record.xml 的視圖元件
    inner class RecordViewHolder(val binding: ItemAdminRecordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = ItemAdminRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecordViewHolder(binding)
    }

    override fun getItemCount(): Int = records.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        holder.binding.apply {
            // 這裡的 ID (tvUsername, tvUnit 等) 需要對應您 item_admin_record.xml 檔案中的 ID
            tvRecordUsername.text = "使用者: ${record.username}"
            tvRecordUnit.text = "單元: ${if (record.unit == 0) "全部" else record.unit.toString()}"
            tvRecordScore.text = "分數: ${record.score}"
            tvRecordTimeSpent.text = "耗時: ${record.timeSpent} 秒"
            tvRecordDate.text = "測驗時間: ${record.recordTime}"
        }
    }

    // 新增的方法：用來更新 RecyclerView 的資料
    fun updateData(newRecords: List<TestResult>) {
        this.records = newRecords
        notifyDataSetChanged() // 通知 Adapter 資料已變動，需要重繪
    }
}
