package com.example.mealdb.country.domain

class CountryInteractor(
    val gateway: CountryGateway
) {

    suspend fun fetchData(): CountryEntity {
        return gateway.request()
    }
}