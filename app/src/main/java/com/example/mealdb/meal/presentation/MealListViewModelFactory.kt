package com.example.mealdb.meal.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealListViewModelFactory(
    val context: Context,
    val categoryName: String?,
    val flag: String?,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealListViewModel(context, categoryName, flag) as T
    }
}