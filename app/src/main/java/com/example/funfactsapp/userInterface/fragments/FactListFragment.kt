package com.example.funfactsapp.userInterface.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funfactsapp.R
import com.example.funfactsapp.databinding.FragmentFactListBinding
import com.example.funfactsapp.userInterface.adapters.FactAdapter
import com.example.funfactsapp.viewmodel.FactViewModel
import com.example.funfactsapp.viewmodel.FactViewModelFactory
import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.data.repository.FactRepository
import com.google.android.material.transition.MaterialSharedAxis

class FactListFragment : Fragment() {

    private var _binding: FragmentFactListBinding? = null
    private val binding get() = _binding!!

    // ✅ Create FactRepository and ViewModelFactory
    private val factRepository by lazy { FactRepository(AppDatabase.getDatabase(requireContext()).factDao()) }
    private val viewModelFactory by lazy { FactViewModelFactory(factRepository) }
    private val factViewModel: FactViewModel by viewModels { viewModelFactory }

    private lateinit var factAdapter: FactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Apply shared axis transition for smooth fragment transitions
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Initialize MotionLayout (if applicable)
        val motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.transitionToStart()

        // ✅ Initialize adapter
        factAdapter = FactAdapter(
            onFavoriteClick = { fact -> factViewModel.saveToFavorites(fact) },
            favoriteFactIds = emptySet()
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }

        // ✅ Observe facts and update RecyclerView
        factViewModel.facts.observe(viewLifecycleOwner) { facts ->
            Log.d("FactListFragment", "Loaded facts: ${facts.size}")
            factAdapter.submitList(facts)
        }

        // ✅ Observe favorite facts and update adapter dynamically
        factViewModel.favoriteFacts.observe(viewLifecycleOwner) { favorites ->
            factAdapter.updateFavorites(favorites)
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