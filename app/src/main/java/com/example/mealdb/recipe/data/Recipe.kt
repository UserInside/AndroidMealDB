package receipt.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import receipt.domain.RecipeEntity
import receipt.domain.RecipeGateway

class RecipeGatewayImplementation(
    private val httpClient: RecipeHttpClient
) : RecipeGateway {

    override suspend fun request(): RecipeEntity {
        return map(httpClient.request())
    }
}

fun map(recipe: Recipe?): RecipeEntity {
    return RecipeEntity(recipe)
}

class RecipeHttpClient(
    private val id: String?
) {
    private var recipe: Recipe? = null

    suspend fun request(): Recipe? {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        val response: HttpResponse =
            client.get("https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id")
        recipe = response.body()
        client.close()

        return recipe
    }
}

@Serializable
data class Recipe(val meals: List<RecipeItem>?)

@Serializable
data class RecipeItem(
    val idMeal: String?,
    val strMeal: String?,
    val strDrinkAlternate: String?,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    val strTags: String?,
    val strYoutube: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strIngredient16: String?,
    val strIngredient17: String?,
    val strIngredient18: String?,
    val strIngredient19: String?,
    val strIngredient20: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?,
    val strMeasure16: String?,
    val strMeasure17: String?,
    val strMeasure18: String?,
    val strMeasure19: String?,
    val strMeasure20: String?,
    val strSource: String?,
    val strImageSource: String?,
    val strCreativeCommonsConfirmed: String?,
    val dateModified: String?,
) {

    private val ingredientsList = listOf(
        Ingredient(strIngredient1, strMeasure1),
        Ingredient(strIngredient2, strMeasure2),
        Ingredient(strIngredient3, strMeasure3),
        Ingredient(strIngredient4, strMeasure4),
        Ingredient(strIngredient5, strMeasure5),
        Ingredient(strIngredient6, strMeasure6),
        Ingredient(strIngredient7, strMeasure7),
        Ingredient(strIngredient8, strMeasure8),
        Ingredient(strIngredient9, strMeasure9),
        Ingredient(strIngredient10, strMeasure10),
        Ingredient(strIngredient11, strMeasure11),
        Ingredient(strIngredient12, strMeasure12),
        Ingredient(strIngredient13, strMeasure13),
        Ingredient(strIngredient14, strMeasure14),
        Ingredient(strIngredient15, strMeasure15),
        Ingredient(strIngredient16, strMeasure16),
        Ingredient(strIngredient17, strMeasure17),
        Ingredient(strIngredient18, strMeasure18),
        Ingredient(strIngredient19, strMeasure19),
        Ingredient(strIngredient20, strMeasure20),
    )

    val ingredients: List<Ingredient> = ingredientsList
        .filter { it.strIngredient != "" }.filter{ it.strIngredient != null }

    // tags
    val tags = strTags?.split(",")
}

@Serializable
data class Ingredient(
    val strIngredient: String?,
    val strMeasure: String?,
)