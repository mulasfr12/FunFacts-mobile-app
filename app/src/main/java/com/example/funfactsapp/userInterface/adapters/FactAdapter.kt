package com.example.funfactsapp.userInterface.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.databinding.ItemFactBinding

class FactAdapter(
    private val onFavoriteClick: (Fact) -> Unit,
    private var favoriteFactIds: Set<Int> = emptySet() // ✅ Accepts favorite fact IDs
) : ListAdapter<Fact, FactAdapter.FactViewHolder>(FactDiffCallback()) {

    inner class FactViewHolder(private val binding: ItemFactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fact: Fact) {
            binding.tvFact.text = fact.text

            // ✅ Ensure favorite button reflects current favorite status
            binding.btnFavorite.isSelected = favoriteFactIds.contains(fact.id)

            binding.btnFavorite.setOnClickListener {
                onFavoriteClick(fact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val binding = ItemFactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // ✅ Method to update favorite fact IDs dynamically
    fun updateFavorites(favorites: List<Fact>) {
        favoriteFactIds = favorites.map { it.id }.toSet()
        notifyDataSetChanged()
    }
}

// ✅ DiffUtil for efficient RecyclerView updates
class FactDiffCallback : DiffUtil.ItemCallback<Fact>() {
    override fun areItemsTheSame(oldItem: Fact, newItem: Fact): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Fact, newItem: Fact): Boolean = oldItem == newItem
}
