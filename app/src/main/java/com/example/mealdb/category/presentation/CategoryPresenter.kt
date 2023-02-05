package category.presentation

import category.data.CategoryList
import category.domain.CategoryEntity
import category.domain.CategoryInteractor
import meal.data.MealGatewayImplementation
import meal.data.MealHttpClient
import meal.domain.MealInteractor
import meal.presentation.MealPresenter

class CategoryPresenter(
    private val interactor: CategoryInteractor
) {
    private var data: CategoryEntity? = null

    suspend fun initialAction() {
        data = interactor.fetchData()
        println(
            "Choose your destiny! (Type)\n" +
                    "1 - Show List of Categories \n"
        )
        val input = readln()
        when (input) {
            "1" -> {
                showCategoryList(data)
                chooseCategory()
            }
        }
    }

    private suspend fun showCategoryList(data: CategoryEntity?) {
        println(data?.categoryList)
        chooseCategory()
    }


suspend fun chooseCategory() {
    println(
        "Type and press Enter \n" +
                "\"1 (space) CategoryName\" to open required category \n" +
                "\"2 (space) SomeText\" to find by category names \n" +
                "8 - Sort categories by name \n" +
                "9 - Descending sort categories by name \n"
    )
    val input = readln()
    when (input.first()) {
        '1' -> showCategory(input.split(" ")[1])

        '2' -> {
            showCategoryList(interactor.filterCategoryList(input.split(" ")[1], data))
        }

        '8' -> {
            showCategoryList(interactor.sortByName(data))
        }
        '9' -> {
            showCategoryList(interactor.sortDescendingByName(data))
        }

        else -> {
            print("Incorrect input")
            showCategoryList(data)
        }
    }
}


}
// ShowMealList
suspend fun showCategory (categoryName: String) {
    val httpClient = MealHttpClient(categoryName)
    val gateway = MealGatewayImplementation(httpClient)
    val interactor = MealInteractor(gateway)
    val presenter = MealPresenter(interactor)
    presenter.initialAction()
}