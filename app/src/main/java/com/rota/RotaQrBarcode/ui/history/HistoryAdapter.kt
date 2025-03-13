package com.rota.RotaQrBarcode.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rota.RotaQrBarcode.databinding.ItemHistoryBinding
import com.rota.RotaQrBarcode.model.ScanItem

class HistoryAdapter(
    private val historyList: List<ScanItem>,
    private val onItemClicked: (ScanItem) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScanItem) {
            binding.textViewScanData.text = item.data
            binding.root.setOnClickListener { onItemClick?.invoke(item) }
        }

        private var onItemClick: ((ScanItem) -> Unit)? = null

        fun setOnItemClickListener(listener: (ScanItem) -> Unit) {
            onItemClick = listener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = historyList.size
}
