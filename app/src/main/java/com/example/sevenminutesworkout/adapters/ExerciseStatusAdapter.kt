package com.example.sevenminutesworkout.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sevenminutesworkout.R
import com.example.sevenminutesworkout.databinding.ItemExerciseStatusBinding
import com.example.sevenminutesworkout.data.models.Exercise

class ExerciseStatusAdapter(val items: ArrayList<Exercise>) :
    RecyclerView.Adapter<ExerciseStatusAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemExerciseStatusBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExerciseStatusBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /** will be created 12 items, cause we have 12 elements
     * it also triggers on notifyDataSetChanged */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: Exercise = items[position]
        holder.tvItem.text = model.id.toString()

        when {
            model.isSelected -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.item_circular_thin_color_accent_border
                    )
                holder.tvItem.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.dark_gray
                    )
                )
            }
            model.isCompleted -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.item_circular_color_accent_background
                    )
                holder.tvItem.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.white
                    )
                )
            }
            else -> {
                holder.tvItem.background =
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.item_circular_color_gray_background
                    )
                holder.tvItem.setTextColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.dark_gray
                    )
                )
            }

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}