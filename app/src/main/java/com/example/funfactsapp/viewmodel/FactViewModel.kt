package com.example.funfactsapp.viewmodel

import androidx.lifecycle.*
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.repository.FactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FactViewModel(private val repository: FactRepository) : ViewModel() {

    private val _facts = MutableLiveData<List<Fact>>()
    val facts: LiveData<List<Fact>> get() = _facts

    // ✅ Use StateFlow instead of MutableLiveData for better efficiency
    private val _favoriteFacts = MutableLiveData<List<Fact>>()
    val favoriteFacts: LiveData<List<Fact>> get() = _favoriteFacts

    init {
        getFavoriteFacts() // ✅ Automatically fetch favorites when ViewModel starts
    }

    fun fetchRandomFacts(count: Int = 5) {
        viewModelScope.launch {
            _facts.postValue(repository.fetchRandomFacts(count))
        }
    }

    fun getFavoriteFacts() {
        viewModelScope.launch {
            repository.getFavoriteFacts().collectLatest { favorites ->
                _favoriteFacts.postValue(favorites)
            }
        }
    }

    fun isFavorite(factId: Int): LiveData<Boolean> {
        return isFavoriteFlow(factId).asLiveData() // ✅ Convert Flow to LiveData
    }
    private fun isFavoriteFlow(factId: Int): Flow<Boolean> {
        return repository.isFavorite(factId)
    }
    fun saveToFavorites(fact: Fact) {
        viewModelScope.launch {
            repository.markAsFavorite(fact.id)
            refreshFavorites() // ✅ Efficiently refresh only the needed data
        }
    }

    fun removeFromFavorites(fact: Fact) {
        viewModelScope.launch {
            repository.unmarkFavorite(fact.id)
            refreshFavorites()
        }
    }

    private fun refreshFavorites() {
        viewModelScope.launch {
            _favoriteFacts.postValue(repository.getFavoriteFacts().stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            ).value)
        }
    }
}
