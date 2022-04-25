package com.example.sevenminutesworkout.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.data.models.History
import com.example.sevenminutesworkout.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val histories: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemHistoryRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llItemHistory = binding.llItemHistory
        val tvNumber = binding.tvNumber
        val tvDate = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = histories[position]
        val ctx = holder.itemView.context
        holder.tvNumber.text = "${position + 1}"
        holder.tvDate.text = date.date
        if (position % 2 == 0) {
            holder.llItemHistory.setBackgroundColor(
                ContextCompat.getColor(
                    ctx,
                    R.color.lightest_gray
                )
            )
        } else {
            holder.llItemHistory.setBackgroundColor(
                ContextCompat.getColor(
                    ctx,
                    R.color.white
                )
            )
        }


    }

    override fun getItemCount(): Int {
        return histories.size
    }
}