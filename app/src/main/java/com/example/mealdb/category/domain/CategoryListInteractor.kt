package category.domain

import com.example.mealdb.category.data.CategoryList
import com.example.mealdb.category.domain.CategoryListRepository

class CategoryListInteractor(
    private val gateway: CategoryListRepository
) {
    private var categoryListEntity: CategoryListEntity? = null

    suspend fun fetchData(): CategoryListEntity {
        categoryListEntity = gateway.fetchCategoryList()
        return categoryListEntity as CategoryListEntity
    }

    fun sortByName(data: CategoryListEntity?): CategoryListEntity {
        return CategoryListEntity(CategoryList(data?.categoryList?.categories?.sortedBy { it.strCategory }))
    }

    fun sortDescendingByName(data: CategoryListEntity?): CategoryListEntity {
        return CategoryListEntity(CategoryList(data?.categoryList?.categories?.sortedByDescending { it.strCategory }))
    }

    fun filterCategoryList(query: String?): CategoryListEntity {
        return CategoryListEntity(
            CategoryList(categoryListEntity?.categoryList?.categories?.filter {
                it.strCategory?.lowercase()?.contains(query?.lowercase() ?: "") ?: false
            })
        )
    }

}

