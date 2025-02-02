package com.example.funfactsapp.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.repository.FactRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FactRepository) : ViewModel() {

    // LiveData for favorite facts
    private val _favorites = MutableLiveData<List<Fact>>()
    val favorites: LiveData<List<Fact>> get() = _favorites

    // Get favorite facts (Flow for real-time updates)
    fun getFavoriteFacts() {
        viewModelScope.launch {
            repository.getFavoriteFacts().collect { favoriteFacts ->
                _favorites.postValue(favoriteFacts)
                Log.d("FavoritesViewModel", "Retrieved favorite facts: ${favoriteFacts.map { it.text }}")
            }
        }
    }

    // Remove fact from favorites and refresh list
    fun removeFavorite(fact: Fact) {
        viewModelScope.launch {
            repository.unmarkFavorite(fact.id)
            getFavoriteFacts() // Refresh the list after deletion
        }
    }
}
