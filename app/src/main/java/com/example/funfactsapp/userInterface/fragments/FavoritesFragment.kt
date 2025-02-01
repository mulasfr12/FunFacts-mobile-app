package com.example.funfactsapp.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.repository.FactRepository
import com.example.funfactsapp.databinding.FragmentFavoritesBinding
import com.example.funfactsapp.userInterface.adapters.FactAdapter
import com.example.funfactsapp.viewmodel.FactViewModelFactory
import com.example.funfactsapp.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    // ✅ Initialize Database & Repository for ViewModelFactory
    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val factRepository by lazy { FactRepository(database.factDao()) }

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FactViewModelFactory(factRepository) // ✅ Pass repository to ViewModel
    }

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

        // ✅ Initialize Adapter
        factAdapter = FactAdapter(
            favoriteFactIds = emptySet(), // ✅ Initialize with empty favorites
            onFavoriteClick = { fact -> removeFavorite(fact) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }

// ✅ Observe favorite facts and update adapter dynamically
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            factAdapter.submitList(favorites)
            factAdapter.updateFavorites(favorites) // ✅ Correctly updates favorite IDs
        }


        // ✅ Fetch favorite facts from database
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
