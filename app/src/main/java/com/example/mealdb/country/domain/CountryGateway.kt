package com.example.mealdb.country.domain

import com.example.mealdb.country.data.CountryList

interface CountryGateway {
    suspend fun request(): CountryEntity
}