package category.domain

import category.data.CategoryList

class CategoryInteractor(
    private val gateway: CategoryGateway
) {
    suspend fun fetchData() : CategoryEntity {
        return gateway.request()
    }

    fun sortByName(data: CategoryEntity?): CategoryEntity {
        return CategoryEntity(CategoryList(data?.categoryList?.categories?.sortedBy { it.strCategory }))
    }

    fun sortDescendingByName(data: CategoryEntity?) : CategoryEntity {
        return CategoryEntity(CategoryList(data?.categoryList?.categories?.sortedByDescending { it.strCategory }))
    }

    fun filterCategoryList(text: String, data: CategoryEntity?) : CategoryEntity {
        return CategoryEntity(CategoryList(data?.categoryList?.categories?.filter { it.strCategory!!.lowercase().contains(text.lowercase()) }))
    }

}

