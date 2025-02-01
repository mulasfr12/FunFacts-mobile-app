package com.example.funfactsapp.data.repository

import com.example.funfactsapp.data.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Adjust endpoint as per API documentation
    @GET("random.json?language=en")
    suspend fun getRandomFact(): ApiResponse

    // If you need category-specific facts
    @GET("random.json")
    suspend fun getFactByCategory(@Query("language") language: String = "en"): ApiResponse
}
