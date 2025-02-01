package com.example.funfactsapp.data.model

import com.example.funfactsapp.data.db.Fact

object FactResponseMapper {
    fun mapToFact(apiResponse: ApiResponse?): Fact? {
        return if (apiResponse?.text.isNullOrBlank()) {
            null  // Prevents inserting invalid data
        } else {
            apiResponse?.let {
                Fact(
                    text = it.text,
                    category = apiResponse.category ?: "random"
                )
            }
        }
    }
}

//This function converts an API response to a Fact entity for storage in the database.