package com.example.funfactsapp.data.repository

import com.example.funfactsapp.data.db.Fact
import com.example.funfactsapp.data.db.FactDao
import com.example.funfactsapp.data.model.FactResponseMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FactRepository(private val factDao: FactDao) {

    // Fetch random fact from API and save to DB
    suspend fun fetchRandomFact(): Fact? {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = RetrofitInstance.api.getRandomFact() // Get API response
                val fact = FactResponseMapper.mapToFact(apiResponse) // Convert to Fact
                factDao.insertFact(fact) // Save to database
                fact
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
    // Fetch multiple random facts (workaround for APIs that return one fact at a time)
    suspend fun fetchRandomFacts(count: Int): List<Fact> {
        return withContext(Dispatchers.IO) {
            try {
                val facts = mutableListOf<Fact>()
                repeat(count) {
                    val apiResponse = RetrofitInstance.api.getRandomFact() // Fetch one fact
                    val fact = FactResponseMapper.mapToFact(apiResponse)
                    factDao.insertFact(fact) // Save to database
                    facts.add(fact)
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
                factDao.insertFact(fact)
                fact
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }


    // Get all saved facts from the database
    suspend fun getAllFacts(): List<Fact> {
        return withContext(Dispatchers.IO) {
            factDao.getAllFacts()
        }
    }

    // Get favorite facts
    suspend fun getFavoriteFacts(): List<Fact> {
        return withContext(Dispatchers.IO) {
            factDao.getFavoriteFacts()
        }
    }

    // Update fact's favorite status
    suspend fun updateFavoriteStatus(factId: Int, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                factDao.updateFavoriteStatus(factId, isFavorite)
            } catch (e: Exception) {
                e.printStackTrace() // Log any potential errors
            }
        }
    }

    // Delete all facts
    suspend fun deleteAllFacts() {
        withContext(Dispatchers.IO) {
            try {
                factDao.deleteAllFacts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Save fact to favorites
    suspend fun saveFactToFavorites(fact: Fact) {
        withContext(Dispatchers.IO) {
            try {
                // Create a new copy of the fact with updated isFavorite status
                val updatedFact = fact.copy(isFavorite = true)
                factDao.updateFact(updatedFact) // Update in database
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Delete fact from favorites
    suspend fun removeFactFromFavorites(factId: Int) {
        withContext(Dispatchers.IO) {
            try {
                factDao.updateFavoriteStatus(factId, false) // Set favorite status to false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Fetch facts with caching (offline support)
    suspend fun fetchCachedFacts(): List<Fact> {
        return withContext(Dispatchers.IO) {
            try {
                val facts = factDao.getAllFacts()
                if (facts.isNotEmpty()) {
                    facts // Return cached facts if available
                } else {
                    val response = RetrofitInstance.api.getRandomFact()
                    val fact = FactResponseMapper.mapToFact(response)
                    factDao.insertFact(fact)
                    listOf(fact) // Return the new fact
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList() // Return empty list in case of failure
            }
        }
    }
}
