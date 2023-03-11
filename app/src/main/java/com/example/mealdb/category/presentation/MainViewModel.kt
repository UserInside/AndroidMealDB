package com.example.mealdb.category.presentation

import android.view.View
import androidx.lifecycle.ViewModel
import category.data.CategoryGatewayImplementation
import category.data.CategoryHttpClient
import category.domain.CategoryEntity
import category.domain.CategoryGateway
import category.domain.CategoryInteractor

class MainViewModel : ViewModel() {
    lateinit var interactor: CategoryInteractor


    suspend fun getCategoryEntity(): CategoryEntity {
        val categoryHttpClient = CategoryHttpClient()
        val gateway: CategoryGateway = CategoryGatewayImplementation(categoryHttpClient)
        interactor = CategoryInteractor(gateway)
        val data = interactor.fetchData()
        return data
    }



}