package com.example.mealdb.ingredient.data

import com.example.mealdb.ingredient.domain.IngredientEntity
import com.example.mealdb.ingredient.domain.IngredientGateway
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class IngredientGatewayImplementation(
    val httpClient: IngredientHttpClient
) : IngredientGateway {

    override suspend fun request(): IngredientEntity {
        return IngredientEntity(httpClient.request())
    }
}

class IngredientHttpClient() {
    suspend fun request(): IngredientList {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        val response = client.get("https://www.themealdb.com/api/json/v1/1/list.php?i=list")

        return response.body()
    }
}

@Serializable
data class IngredientList(
    val meals: List<Ingredient>?
)

@Serializable
data class Ingredient(
    val idIngredient: String?,
    val strIngredient: String?,
    val strDescription: String?,
    val strType: String?,
)