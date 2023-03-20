package meal.domain

interface MealListGateway {
    suspend fun request(): MealListEntity
}