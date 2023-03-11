package com.example.mealdb.country.domain

interface CountryGateway {
    suspend fun request(): CountryEntity
}