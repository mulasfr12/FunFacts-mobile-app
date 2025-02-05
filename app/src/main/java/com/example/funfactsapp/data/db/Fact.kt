package com.example.funfactsapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facts")
data class Fact(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,  // ✅ Ensure this exists
    val category: String
)
