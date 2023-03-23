package com.example.mealdb.category.presentation

import category.domain.CategoryListEntity
import com.example.mealdb.ContentState

data class CategoryListUiState(
    val categoryListEntity: CategoryListEntity? = null,
    val contentState: ContentState = ContentState.Idle
) {
}