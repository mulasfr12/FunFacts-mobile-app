package com.example.funfactsapp.data.model

enum class FactCategory(val categoryName: String) {
    HISTORY("history"),
    SCIENCE("science"),
    RANDOM("random"),
    TECHNOLOGY("technology"),
    SPACE("space"),
    ANIMALS("animals");

    companion object {
        fun fromString(category: String): FactCategory {
            return values().find { it.categoryName.equals(category, ignoreCase = true) } ?: RANDOM
        }
    }
}
//This helps in categorizing facts based on API responses.