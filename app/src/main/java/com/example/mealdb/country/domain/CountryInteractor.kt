package com.example.mealdb.country.domain

import com.example.mealdb.country.data.CountryList

class CountryInteractor(
    val gateway: CountryGateway
) {

    suspend fun fetchData(): CountryEntity {
        return gateway.request()
    }

    fun filterCountryList(text: String?, data: CountryEntity?): CountryEntity {
        val countryList = CountryList(data?.countryList?.meals?.filter {
            it.strArea?.lowercase()
                ?.contains(text?.lowercase() ?: "") ?: false
        })
        return CountryEntity(countryList)
    }

    fun sortAscendingByName(data: CountryEntity?) : CountryEntity {
        return CountryEntity(CountryList(data?.countryList?.meals?.sortedBy { it.strArea }))
    }

    fun sortDescendingByName(data: CountryEntity?) : CountryEntity {
        return CountryEntity(CountryList(data?.countryList?.meals?.sortedByDescending { it.strArea }))
    }
}