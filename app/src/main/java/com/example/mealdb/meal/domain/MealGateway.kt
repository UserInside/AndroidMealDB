package meal.domain

interface MealGateway {
    suspend fun request() : MealEntity
}