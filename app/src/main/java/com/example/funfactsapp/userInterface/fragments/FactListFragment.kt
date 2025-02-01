package com.example.funfactsapp.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funfactsapp.databinding.FragmentFactListBinding
import com.example.funfactsapp.userInterface.adapters.FactAdapter
import com.example.funfactsapp.viewmodel.FactViewModel
import com.example.funfactsapp.viewmodel.FactViewModelFactory
import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.data.repository.FactRepository

class FactListFragment : Fragment() {

    private var _binding: FragmentFactListBinding? = null
    private val binding get() = _binding!!

    // ✅ Create FactRepository and ViewModelFactory
    private val factRepository by lazy { FactRepository(AppDatabase.getDatabase(requireContext()).factDao()) }
    private val viewModelFactory by lazy { FactViewModelFactory(factRepository) }
    private val factViewModel: FactViewModel by viewModels { viewModelFactory }

    private lateinit var factAdapter: FactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Initialize adapter with an empty favorite list initially
        factAdapter = FactAdapter(
            onFavoriteClick = { fact -> factViewModel.saveToFavorites(fact) },
            favoriteFactIds = emptySet() // ✅ Initialize with an empty set
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }

        // ✅ Observe facts and update RecyclerView
        factViewModel.facts.observe(viewLifecycleOwner) { facts ->
            factAdapter.submitList(facts)
        }

        // ✅ Observe favorite facts and update adapter dynamically
        factViewModel.favoriteFacts.observe(viewLifecycleOwner) { favorites ->
            factAdapter.updateFavorites(favorites) // ✅ Ensure favorites update dynamically
        }

        // Fetch facts and favorite facts
        factViewModel.fetchRandomFacts()
        factViewModel.getFavoriteFacts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
