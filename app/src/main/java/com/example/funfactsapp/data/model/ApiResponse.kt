package com.example.funfactsapp.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("text") val text: String,
    @SerializedName("category") val category: String? = "random"
)//This class is used to parse API responses from external services like NumbersAPI or Useless Facts API.