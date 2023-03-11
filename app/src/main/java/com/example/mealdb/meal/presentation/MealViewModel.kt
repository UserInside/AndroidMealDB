package com.example.mealdb.meal.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import meal.data.MealGatewayImplementation
import meal.data.MealHttpClient
import meal.data.MealList
import meal.domain.MealEntity
import meal.domain.MealInteractor

class MealViewModel(
    val context: Context,
    val categoryName: String?,
    val flag: String?,
) : ViewModel() {

    var mealList: MealList? = null
    lateinit var interactor: MealInteractor

    init {
        viewModelScope.launch {
            mealList = getMealEntity()?.meal
        }
    }

    suspend fun getMealEntity(): MealEntity? {
        val mealHttpClient = MealHttpClient(categoryName, flag)
        val gateway = MealGatewayImplementation(mealHttpClient)
        interactor = MealInteractor(gateway)
        val request = interactor.fetchData()
        mealList = request.meal
        return MealEntity(mealList)
    }
}