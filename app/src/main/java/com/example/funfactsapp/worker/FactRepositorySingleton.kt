package com.example.funfactsapp.worker

import com.example.funfactsapp.data.db.AppDatabase
import com.example.funfactsapp.data.repository.FactRepository

object FactRepositorySingleton {
    lateinit var repository: FactRepository

    fun initialize(database: AppDatabase) {
        repository = FactRepository(database.factDao())
    }
}
