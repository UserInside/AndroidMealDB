package meal.domain

import meal.data.MealList

class MealListInteractor(
    private val gateway: MealListGateway
) {
    var dataOrigin: MealListEntity? = null
    var data: MealListEntity? = null

    suspend fun fetchData(): MealListEntity {
        dataOrigin = gateway.request()
        data = dataOrigin
        return dataOrigin as MealListEntity
    }

    fun sortByName(data: MealListEntity?) :MealListEntity {
        return MealListEntity(MealList(dataOrigin?.mealList?.meals?.sortedBy { it.strMeal }))
    }

    fun sortDescendingByName(data: MealListEntity?) : MealListEntity {
        return MealListEntity(MealList(dataOrigin?.mealList?.meals?.sortedByDescending { it.strMeal }))
    }

    fun filterMealList(text: String) : MealListEntity {
        return MealListEntity(MealList(dataOrigin?.mealList?.meals?.filter { it.strMeal?.lowercase()?.contains(text.lowercase()) ?: false }))
    }
}