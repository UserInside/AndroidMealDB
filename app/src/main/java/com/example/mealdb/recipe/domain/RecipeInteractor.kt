package receipt.domain

class RecipeInteractor(
    private val gateway: RecipeGateway
) {

    suspend fun fetchRecipe(): RecipeEntity {
        return gateway.request()
    }
}