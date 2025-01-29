package com.example.funfactsapp.userInterface.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.databinding.FragmentFactListBinding
import com.example.funfactsapp.ui.adapters.FactAdapter

class FactListFragment : Fragment() {

    private var _binding: FragmentFactListBinding? = null
    private val binding get() = _binding!!
    private lateinit var factAdapter: FactAdapter
    private val facts = mutableListOf<Fact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factAdapter = FactAdapter(facts) { fact -> saveToFavorites(fact) }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = factAdapter
        }
    }

    private fun saveToFavorites(fact: Fact) {
        fact.isFavorite = !fact.isFavorite // Now this works because isFavorite is a var
        factAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
