package com.example.mealdb.category.domain

import category.domain.CategoryListEntity

interface CategoryListRepository {
    suspend fun fetchCategoryList(): CategoryListEntity
}