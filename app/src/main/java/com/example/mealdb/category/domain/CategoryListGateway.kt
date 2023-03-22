package category.domain

interface CategoryListGateway {
    suspend fun request(): CategoryListEntity
}