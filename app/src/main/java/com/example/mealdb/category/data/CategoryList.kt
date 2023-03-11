package category.data


import category.domain.CategoryEntity
import category.domain.CategoryGateway
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


class CategoryGatewayImplementation(
    private val categoryHttpClient: CategoryHttpClient
) : CategoryGateway {

    override suspend fun request(): CategoryEntity {
        return map(categoryHttpClient.request())
    }
}

fun map(from: CategoryList?): CategoryEntity {
    return CategoryEntity(from)
}

class CategoryHttpClient {


    suspend fun request(): CategoryList? {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        val response: HttpResponse =
            client.get("https://www.themealdb.com/api/json/v1/1/categories.php")
        val categoryList = response.body<CategoryList?>()

        client.close()

        return categoryList
    }
}

@Serializable
data class CategoryList(
    val categories: List<CategoryItem>?
)

@Serializable
data class CategoryItem(
    val idCategory: String?,
    val strCategory: String?,
    val strCategoryThumb: String?,
    val strCategoryDescription: String?,
)