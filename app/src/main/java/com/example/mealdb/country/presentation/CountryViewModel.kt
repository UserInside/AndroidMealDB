package com.example.mealdb.country.presentation

import androidx.lifecycle.ViewModel
import com.example.mealdb.country.data.CountryGatewayImplementation
import com.example.mealdb.country.data.CountryHttpClient
import com.example.mealdb.country.domain.CountryEntity
import com.example.mealdb.country.domain.CountryInteractor

class CountryViewModel : ViewModel() {
    lateinit var interactor : CountryInteractor

    suspend fun getCountryEntity(): CountryEntity {
        val client = CountryHttpClient()
        val gateway = CountryGatewayImplementation(client)
        interactor = CountryInteractor(gateway)
        val data = interactor.fetchData()
        return data
    }
}