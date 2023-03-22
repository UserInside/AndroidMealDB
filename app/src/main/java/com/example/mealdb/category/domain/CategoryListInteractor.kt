package category.domain

import category.data.CategoryList

class CategoryListInteractor(
    private val gateway: CategoryListGateway
) {
    suspend fun fetchData(): CategoryListEntity {
        return gateway.request()
    }

    fun sortByName(data: CategoryListEntity?): CategoryListEntity {
        return CategoryListEntity(CategoryList(data?.categoryList?.categories?.sortedBy { it.strCategory }))
    }

    fun sortDescendingByName(data: CategoryListEntity?): CategoryListEntity {
        return CategoryListEntity(CategoryList(data?.categoryList?.categories?.sortedByDescending { it.strCategory }))
    }

    fun filterCategoryList(text: String?, data: CategoryListEntity?): CategoryListEntity {
        return CategoryListEntity(CategoryList(data?.categoryList?.categories?.filter {
            it.strCategory?.lowercase()?.contains(text?.lowercase() ?: "") ?: false
        }))
    }

}

