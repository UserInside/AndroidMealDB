package com.example.mealdb.ingredient.domain

interface IngredientGateway {
    suspend fun request(): IngredientEntity
}
