package meal.domain

import meal.data.MealList

class MealInteractor(
    private val gateway: MealGateway
) {
    var dataOrigin : MealEntity? = null
    var data : MealEntity? = null

    suspend fun fetchData(): MealEntity {
        dataOrigin = gateway.request()
        data = dataOrigin
        return dataOrigin as MealEntity
    }

    fun sortByName() {
        data = MealEntity(MealList(dataOrigin?.meal?.meals?.sortedBy { it.strMeal }))
    }

    fun sortDescendingByName() {
        data = MealEntity(MealList(dataOrigin?.meal?.meals?.sortedByDescending { it.strMeal }))
    }

    fun filterMealList(text: String) {
        data = MealEntity(MealList(dataOrigin?.meal?.meals?.filter { it.idMeal!!.contains(text) } ))
    }
}