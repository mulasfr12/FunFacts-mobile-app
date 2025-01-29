package com.example.funfactsapp.data.db

import androidx.room.*

@Dao
interface FactDao {

    @Query("SELECT * FROM facts ORDER BY id DESC")
    suspend fun getAllFacts(): List<Fact>

    @Query("SELECT * FROM facts WHERE isFavorite = 1")
    suspend fun getFavoriteFacts(): List<Fact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: Fact)

    @Delete
    suspend fun deleteFact(fact: Fact)

    @Query("SELECT * FROM facts ORDER BY id DESC LIMIT :limit")
    suspend fun getCachedFacts(limit: Int): List<Fact>

    @Query("SELECT * FROM facts WHERE category = :category ORDER BY id DESC LIMIT :limit")
    suspend fun getCachedFactsByCategory(category: String, limit: Int): List<Fact>

    @Query("DELETE FROM facts")
    suspend fun deleteAllFacts()

    @Query("UPDATE facts SET isFavorite = :isFavorite WHERE id = :factId")
    suspend fun updateFavoriteStatus(factId: Int, isFavorite: Boolean)
    @Update
    suspend fun updateFact(fact: Fact)
}