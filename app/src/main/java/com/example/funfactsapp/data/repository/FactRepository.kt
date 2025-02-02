package com.example.funfactsapp.data.repository

import android.util.Log
import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.db.FactDao
import com.example.funfactsapp.data.db.FavoriteFact
import com.example.funfactsapp.data.model.FactResponseMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FactRepository(private val factDao: FactDao) {

    // Fetch random fact from API and save to DB
    suspend fun fetchRandomFact(): Fact? {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = RetrofitInstance.api.getRandomFact()
                val fact = FactResponseMapper.mapToFact(apiResponse)
                fact?.let {
                    val id = factDao.insertFact(it).toInt() // ✅ Assign the generated ID
                    it.copy(id = id) // ✅ Return fact with the correct ID
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun removeFact(factId: Int) {
        withContext(Dispatchers.IO) {
            factDao.deleteFact(factId)  // ✅ No need to store return value
            println("Deleted fact with ID: $factId")
        }
    }

    // Fetch multiple random facts
    suspend fun fetchRandomFacts(count: Int): List<Fact> {
        return withContext(Dispatchers.IO) {
            try {
                val facts = mutableListOf<Fact>()
                repeat(count) {
                    val apiResponse = RetrofitInstance.api.getRandomFact()
                    val fact = FactResponseMapper.mapToFact(apiResponse)
                    fact?.let {
                        val id = factDao.insertFact(it).toInt() // ✅ Assign the generated ID
                        facts.add(it.copy(id = id)) // ✅ Store facts with correct IDs
                    }
                }
                facts
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Fetch fact by category and save to DB
    suspend fun fetchFactByCategory(category: String): Fact? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getFactByCategory(category)
                val fact = FactResponseMapper.mapToFact(response)
                fact?.let { factDao.insertFact(it) }
                fact
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // Get all saved facts from database (using Flow for real-time updates)
    fun getAllFacts(): Flow<List<Fact>> {
        return factDao.getAllFacts()
    }

    // Get favorite facts (JOIN with favorite_facts table)
    fun getFavoriteFacts(): Flow<List<Fact>> {
        return factDao.getFavoriteFacts().onEach { favorites ->
            Log.d("FactRepository", "Retrieved ${favorites.size} favorite facts from DB")
        }
    }


    // Mark a fact as favorite (Insert into favorite_facts table)
    suspend fun markAsFavorite(factId: Int) {
        withContext(Dispatchers.IO) {
            try {
                factDao.markAsFavorite(FavoriteFact(factId))
                Log.d("FactRepository", "Marked as favorite: Fact ID $factId")
            } catch (e: Exception) {
                Log.e("FactRepository", "Error marking favorite: ${e.message}", e)
            }
        }
    }


    // Remove a fact from favorites (Delete from favorite_facts table)
    suspend fun unmarkFavorite(factId: Int) {
        withContext(Dispatchers.IO) {
            val rowsDeleted = factDao.unmarkFavorite(factId)
            println("Removed $rowsDeleted favorite fact(s)")
        }
    }

    // Check if a fact is marked as favorite
    fun isFavorite(factId: Int): Flow<Boolean> {
        return factDao.isFavorite(factId).map { count -> count > 0 } // ✅ Convert Int to Boolean
    }

    // Delete all facts from database
    fun clearAllFacts() {
        CoroutineScope(Dispatchers.IO).launch {
            val rowsDeleted = factDao.deleteAllFacts()
            println("Deleted $rowsDeleted facts from the database")
        }
    }


    // Fetch cached facts with offline support (Flow for real-time updates)
    fun fetchCachedFacts(): Flow<List<Fact>> {
        return factDao.getAllFacts()
    }
}
