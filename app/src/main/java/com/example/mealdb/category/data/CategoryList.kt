package com.example.mealdb.category.data

import category.domain.CategoryListEntity
import com.example.mealdb.category.domain.CategoryListRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

class CategoryListGatewayImplementation(
    private val categoryHttpClient: CategoryListDataSource,
) : CategoryListRepository {

    override suspend fun fetchCategoryList(): CategoryListEntity {
        return map(categoryHttpClient.request())
    }
}

fun map(from: CategoryList?): CategoryListEntity {
    return CategoryListEntity(from)
}

class CategoryListDataSource(
    private val httpClient: HttpClient
) {
    suspend fun request(): CategoryList? {
        return httpClient
            .get("https://www.themealdb.com/api/json/v1/1/categories.php")
            .body()
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