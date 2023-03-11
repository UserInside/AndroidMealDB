package com.example.mealdb.recipe.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeViewModelFactory(
    val context: Context,
    val mealId: String?,
) : ViewModelProvider.Factory {



    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return RecipeViewModel(context, mealId) as T
    }
}