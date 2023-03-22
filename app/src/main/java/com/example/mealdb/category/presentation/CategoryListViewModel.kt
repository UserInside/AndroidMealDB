package com.example.mealdb.category.presentation

import androidx.lifecycle.ViewModel
import category.data.CategoryListGatewayImplementation
import category.data.CategoryHttpClient
import category.domain.CategoryListEntity
import category.domain.CategoryListGateway
import category.domain.CategoryListInteractor

class CategoryListViewModel : ViewModel() {
    lateinit var interactor: CategoryListInteractor


    suspend fun getCategoryEntity(): CategoryListEntity {
        val categoryHttpClient = CategoryHttpClient()
        val gateway: CategoryListGateway = CategoryListGatewayImplementation(categoryHttpClient)
        interactor = CategoryListInteractor(gateway)
        val data = interactor.fetchData()
        return data
    }



}