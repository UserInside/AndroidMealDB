package meal.domain

import kotlinx.coroutines.runBlocking
import meal.data.MealItem
import meal.data.MealList
import org.junit.jupiter.api.Assertions.*


class MealInteractorTest {
    private val interactor = MealInteractor(
        object : MealGateway {                             //TODO что тут значит object и почему фигурные скобки
            override suspend fun request(): MealEntity {
                return MealEntity(                           // делаем имитацию
                    meal = MealList(
                        meals = listOf(
                            MealItem(
                                strMeal = "A",
                                strMealThumb = null,
                                idMeal = "52942",
                            ),
                            MealItem(
                                strMeal = "C",
                                strMealThumb = null,
                                idMeal = "52775",
                            ),
                            MealItem(
                                strMeal = "B",
                                strMealThumb = null,
                                idMeal = "52794",
                            ),
                        )
                    )
                )
            }
        }
    )

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        runBlocking {
            interactor.fetchData()
        }
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        interactor.data = null
    }

    @org.junit.jupiter.api.Test
    fun getData() {
    }

    @org.junit.jupiter.api.Test
    fun setData() {
    }

    @org.junit.jupiter.api.Test
    fun fetchData() {                      //TODO правильна ли логика результата от фетча?
        runBlocking {
            interactor.fetchData()
        }
        assertNotNull(interactor.dataOrigin)
        assertNotNull(interactor.data)
        assertEquals(interactor.dataOrigin, interactor.data)

    }

    @org.junit.jupiter.api.Test
    fun sortByName() {
        interactor.sortByName()
        assertIterableEquals(
            listOf("A", "B", "C"),
            interactor.data?.meal?.meals?.map { it.strMeal }.orEmpty(),
            "Sort Failed"
        )
    }

    @org.junit.jupiter.api.Test
    fun sortDescendingByName() {
        interactor.sortDescendingByName()
        assertIterableEquals(
            listOf("C", "B", "A"),
            interactor.data?.meal?.meals?.map { it.strMeal }.orEmpty(),
            "Sort by descending failed"
        )
    }

    @org.junit.jupiter.api.Test
    fun filterMealList() {
        interactor.filterMealList("27")
        assertIterableEquals(
            listOf("52775", "52794"),
            interactor.data?.meal?.meals?.map { mealItem -> mealItem.idMeal }.orEmpty(),
            "Filter failed"
        )
    }
}