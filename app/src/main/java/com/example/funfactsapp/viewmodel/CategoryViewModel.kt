package com.example.funfactsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.repository.FactRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val repository: FactRepository) : ViewModel() {
    private val _factsByCategory = MutableLiveData<List<Fact>>()
    val factsByCategory: LiveData<List<Fact>> get() = _factsByCategory

    fun fetchFactsByCategory(category: String, count: Int = 5) {
        viewModelScope.launch {
            try {
                val facts = mutableListOf<Fact>()
                repeat(count) { // Fetch `count` facts
                    val fact = repository.fetchFactByCategory(category)
                    if (fact != null) facts.add(fact)
                }
                _factsByCategory.postValue(facts)
            } catch (e: Exception) {
                _factsByCategory.postValue(emptyList()) // Post empty list on failure
            }
        }
    }

}
