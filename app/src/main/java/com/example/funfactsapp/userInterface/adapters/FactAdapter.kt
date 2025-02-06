package com.example.funfactsapp.userInterface.adapters

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.databinding.ItemFactBinding

class FactAdapter(
    private var favoriteFactIds: Set<Int>,
    private val onFavoriteClick: (Fact) -> Unit
) : ListAdapter<Fact, FactAdapter.FactViewHolder>(FactDiffCallback()) {

    inner class FactViewHolder(private val binding: ItemFactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fact: Fact) {
            binding.tvFact.text = fact.text
            binding.btnFavorite.isSelected = favoriteFactIds.contains(fact.id)

            binding.btnFavorite.setOnClickListener {
                onFavoriteClick(fact)
                animateFavoriteButton(binding.btnFavorite)
            }
        }

        // ✅ Favorite button scale & bounce animation
        private fun animateFavoriteButton(view: View) {
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.3f, 1f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.3f, 1f)

            val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
            animator.duration = 300
            animator.repeatCount = 0
            animator.start()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactViewHolder {
        val binding = ItemFactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FactViewHolder, position: Int) {
        val fadeIn = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f)
        fadeIn.duration = 500
        fadeIn.start()
        holder.bind(getItem(position))
    }

    fun updateFavorites(favorites: List<Fact>) {
        favoriteFactIds = favorites.map { it.id }.toSet()
        //notifyDataSetChanged()
    }
}

// ✅ DiffUtil for efficient RecyclerView updates
class FactDiffCallback : DiffUtil.ItemCallback<Fact>() {
    override fun areItemsTheSame(oldItem: Fact, newItem: Fact): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Fact, newItem: Fact): Boolean = oldItem == newItem
}
