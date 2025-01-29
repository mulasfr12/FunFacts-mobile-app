package com.example.funfactsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.databinding.ItemFactBinding

class FactAdapter(private val facts: List<Fact>, private val onFavoriteClick: (Fact) -> Unit) :
    RecyclerView.Adapter<FactAdapter.FactViewHolder>() {

    inner class FactViewHolder(private val binding: ItemFactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fact: Fact) {
            binding.tvFact.text = fact.text
            binding.btnFavorite.setOnClickListener { onFavoriteClick(fact) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val binding = ItemFactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        holder.bind(facts[position])
    }

    override fun getItemCount() = facts.size
}
