package com.example.ticker.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ticker.databinding.LayoutDefaultTickerTimeBinding
import com.example.ticker.utils.toDoubleDigitString

class TickerTimeAdapter : ListAdapter<Int, TickerTimeAdapter.DefaultTickerViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultTickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutDefaultTickerTimeBinding.inflate(inflater, parent, false)
        return DefaultTickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefaultTickerViewHolder, position: Int) {
        val time = getItem(position)
        holder.bindTime(time)
    }

    inner class DefaultTickerViewHolder(val binding: LayoutDefaultTickerTimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTime(position: Int) {
            binding.root.text = position.toDoubleDigitString()
        }

    }
}
