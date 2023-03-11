package com.example.mealdb.meal.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealViewModelFactory(
    val context: Context,
    val categoryName: String?,
    val flag: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(context, categoryName, flag) as T
    }
}