package meal.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import meal.domain.MealListEntity
import meal.domain.MealListGateway

class MealGatewayImplementation(
    private val httpClient: MealHttpClient
) : MealListGateway {

    override suspend fun request(): MealListEntity {
        return map(httpClient.request())
    }
}

fun map(from: MealList?): MealListEntity {
    return MealListEntity(from)
}

class MealHttpClient(
    private val categoryName: String?,
    private val flag: String?
) {
    private var mealList: MealList? = null
    private var prefix: String? = null

    suspend fun request(): MealList? {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        when (flag) {
            "category" -> prefix = "c="
            "area" -> prefix = "a="
            "ingredient" -> prefix = "i="

        }
        val response: HttpResponse =
            client.get("https://www.themealdb.com/api/json/v1/1/filter.php?$prefix$categoryName")
        mealList = response.body()
        client.close()

        return mealList

    }
}

@Serializable
data class MealList(
    val meals: List<MealItem>?
)

@Serializable
data class MealItem(
    val strMeal: String?,
    val strMealThumb: String?,
    val idMeal: String?,
)