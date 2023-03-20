package com.example.mealdb.recipe.presentation

import com.example.mealdb.ContentState
import receipt.data.RecipeItem


data class RecipeUiState(
    var recipeItem: RecipeItem? = null,
    var foodImage: String? = null,
    var foodImagePreview: String? = null,
    val contentState: ContentState = ContentState.Idle
)

