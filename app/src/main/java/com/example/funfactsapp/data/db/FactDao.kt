package com.example.funfactsapp.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FactDao {

    @Query("SELECT * FROM facts ORDER BY id DESC")
    fun getAllFacts(): Flow<List<Fact>>

    @Query("SELECT * FROM facts WHERE id IN (SELECT factId FROM favorite_facts)")
    fun getFavoriteFacts(): Flow<List<Fact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact(fact: Fact)

    // ✅ Ensure deleteFact() correctly returns `Int`
    @Query("DELETE FROM facts WHERE id = :factId")
    suspend fun deleteFact(factId: Int): Int  // ✅ Must return Int

    @Query("DELETE FROM facts")
    suspend fun deleteAllFacts(): Int  // ✅ Must return Int

    // Favorite Fact Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun markAsFavorite(favoriteFact: FavoriteFact)

    @Query("DELETE FROM favorite_facts WHERE factId = :factId")
    suspend fun unmarkFavorite(factId: Int): Int  // ✅ Ensure correct return type

    @Query("SELECT COUNT(*) FROM favorite_facts WHERE factId = :factId")
    fun isFavorite(factId: Int): Flow<Int>  // ✅ Uses Flow<Int> for proper Room handling
}
