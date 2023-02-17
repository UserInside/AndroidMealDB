package com.example.mealdb.country.data

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class CountryHttpClient(
    val country: String?
){

//    suspend fun request(): CountryList? {
//        val client = HttpClient(OkHttp){
//            install(ContentNegotiation){
//                json(Json{
//                    prettyPrint = true
//                    isLenient = true
//                })
//            }
//        }
//
////        val resposnse = get //todo
//
//        return CountryList
//    }

}

@Serializable
data class CountryList (
    val meals: List<Country>?
        ) {

}

@Serializable
data class Country(
    val strArea: String?
){

}