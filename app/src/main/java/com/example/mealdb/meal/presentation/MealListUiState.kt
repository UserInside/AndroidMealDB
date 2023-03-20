package com.example.mealdb.meal.presentation

import com.example.mealdb.ContentState
import meal.data.MealList

data class MealListUiState(
    val mealList: MealList? = null, //todo тут может быть не та переменная
    val contentState: ContentState = ContentState.Idle
)

