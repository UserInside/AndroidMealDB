package com.example.mealdb.meal.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import meal.data.MealGatewayImplementation
import meal.data.MealHttpClient
import meal.data.MealList
import meal.domain.MealInteractor

class MealViewModel(
    val context: Context,
    val categoryName: String?,
    val flag: String?,
) : ViewModel() {

    var mealList: MealList? = null

    init {
        viewModelScope.launch {
            mealList = getMealList()
        }
    }

    suspend fun getMealList(): MealList? {
        val mealHttpClient = MealHttpClient(categoryName, flag)
        val gateway = MealGatewayImplementation(mealHttpClient)
        val interactor = MealInteractor(gateway)
        val request = interactor.fetchData()
        mealList = request.meal
        return mealList
    }
}