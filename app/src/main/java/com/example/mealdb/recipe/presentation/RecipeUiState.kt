package com.example.mealdb.recipe.presentation

import com.example.mealdb.ContentState
import kotlinx.serialization.InternalSerializationApi
import receipt.data.RecipeItem


data class RecipeUiState @OptIn(InternalSerializationApi::class) constructor(
    var recipeItem: RecipeItem? = null,
    var foodImage: String? = null,
    var foodImagePreview: String? = null,
    val contentState: ContentState = ContentState.Idle
)

