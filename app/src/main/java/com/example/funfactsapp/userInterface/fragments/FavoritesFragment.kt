package com.example.funfactsapp.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.databinding.FragmentFavoritesBinding
import com.example.funfactsapp.userInterface.adapters.FactAdapter
import com.example.funfactsapp.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private lateinit var factAdapter: FactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Ensure RecyclerView exists in XML
        factAdapter = FactAdapter(
            onFavoriteClick = { fact -> removeFavorite(fact) },
            favoriteFactIds = emptySet() // Initialize empty, update later
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }

        // ✅ Observe favorite facts
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            factAdapter.submitList(favorites)
            factAdapter.updateFavorites(favorites) // Update favorite fact IDs
        }

        // ✅ Fetch favorite facts
        favoritesViewModel.getFavoriteFacts()
    }

    private fun removeFavorite(fact: Fact) {
        favoritesViewModel.removeFavorite(fact)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
