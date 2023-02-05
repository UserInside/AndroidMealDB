package category.domain

interface CategoryGateway {
    suspend fun request(): CategoryEntity
}