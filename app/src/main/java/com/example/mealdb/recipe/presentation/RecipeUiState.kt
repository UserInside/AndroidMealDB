package com.example.mealdb.recipe.presentation

import receipt.data.RecipeItem

data class RecipeUiState(
    var recipeItem: RecipeItem? = null,
    var foodImage: String? = null,
    var foodImagePreview: String? = null,
    val contentState: ContentState = ContentState.Idle
)

sealed class ContentState {
    object Idle : ContentState()
    object Loading : ContentState()
    object Done : ContentState()
    object Error : ContentState()
}