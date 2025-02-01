package com.example.funfactsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.funfactsapp.data.repository.FactRepository

class FactViewModelFactory(private val repository: FactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
