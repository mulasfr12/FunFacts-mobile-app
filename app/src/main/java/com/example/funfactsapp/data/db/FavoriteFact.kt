package com.example.funfactsapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_facts")
data class FavoriteFact(
    @PrimaryKey val factId: Int
)