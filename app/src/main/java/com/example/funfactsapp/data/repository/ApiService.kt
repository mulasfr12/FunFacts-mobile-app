package com.example.funfactsapp.data.repository

import com.example.funfactsapp.data.model.ApiResponse
import com.example.funfactsapp.data.model.FactResponseMapper
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("random")
    suspend fun getRandomFact(): ApiResponse

    @GET("category")
    suspend fun getFactByCategory(@Query("category") category: String): ApiResponse
}
//The getRandomFact() method fetches a random fact.
//The getFactByCategory() method fetches facts based on a specific category.