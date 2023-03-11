package receipt.domain

interface RecipeGateway {
    suspend fun request(): RecipeEntity
}