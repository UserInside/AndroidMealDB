package com.example.mealdb.country.data

import com.example.mealdb.country.domain.CountryEntity
import com.example.mealdb.country.domain.CountryGateway
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class CountryGatewayImplementation(
    val httpClient: CountryHttpClient
): CountryGateway {

    override suspend fun request(): CountryEntity {
        return map(httpClient.request())
    }

}

fun map (item: CountryList): CountryEntity {
    return CountryEntity(item)
}

class CountryHttpClient(){

    suspend fun request(): CountryList {
        val client = HttpClient(OkHttp){
            install(ContentNegotiation){
                json(Json{
                    prettyPrint = true
                    isLenient = true
                })
            }
        }

        val response = client.get("https://www.themealdb.com/api/json/v1/1/list.php?a=list")

        val result: CountryList = response.body()

        return result
    }

}

@Serializable
data class CountryList (
    val meals: List<Country>?
        )

@Serializable
data class Country(
    val strArea: String?
)