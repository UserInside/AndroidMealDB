package category.domain

import category.data.CategoryList

class CategoryListInteractor(
    private val gateway: CategoryListGateway
) {
    private var categoryListEntity : CategoryListEntity? = null

    suspend fun fetchData(): CategoryListEntity {
        categoryListEntity = gateway.request()
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
        }))
    }

}

