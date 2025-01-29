package com.example.funfactsapp.viewmodel.FactViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.repository.FactRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FactRepository) : ViewModel() {
    private val _favorites = MutableLiveData<List<Fact>>()
    val favorites: LiveData<List<Fact>> get() = _favorites

    fun getFavoriteFacts() {
        viewModelScope.launch {
            val favorites = repository.getFavoriteFacts()
            _favorites.postValue(favorites)
        }
    }

    fun removeFavorite(fact: Fact) {
        viewModelScope.launch {
            repository.removeFactFromFavorites(fact.id)
            getFavoriteFacts() // Refresh the list after deletion
        }
    }
}
