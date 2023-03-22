package com.example.mealdb.meal.presentation

import com.example.mealdb.ContentState
import meal.data.MealList
import meal.domain.MealListEntity

data class MealListUiState(
    val mealListEntity: MealListEntity? = null, //todo тут может быть не та переменная
    val contentState: ContentState = ContentState.Idle
)

