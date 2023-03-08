package com.example.mealdb.ingredient.domain

class IngredientInteractor(
    val gateway: IngredientGateway
) {
    suspend fun fetchData(): IngredientEntity {
        return gateway.request()
    }
}