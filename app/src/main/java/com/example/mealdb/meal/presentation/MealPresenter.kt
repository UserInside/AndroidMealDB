package meal.presentation

import meal.domain.MealEntity
import meal.domain.MealInteractor
import receipt.data.ReceiptGatewayImplementation
import receipt.data.ReceiptHttpClient
import receipt.domain.ReceiptInteractor
import receipt.presentation.ReceiptPresenter


class MealPresenter(
    private val interactor: MealInteractor
) {

    suspend fun initialAction() {
        showMealList(interactor.fetchData())
    }

    private suspend fun showMealList(data: MealEntity?) {
        println(data?.meal)
        chooseMeal()
    }

    private suspend fun chooseMeal() {
        println(
            "Type and press Enter \n\n" +
                    "\"1 (space) MealID\" to open required meal \n" +
                    "\"2 (space) PartID\" to find meal by id \n" +

                    "8 - Sort categories by name \n" +
                    "9 - Descending sort categories by name \n"

        )
        val input = readln()
        when (input.first()) {
            '1' -> showReceipt(input.split(" ")[1])
            '2' -> {
                interactor.filterMealList(input.split(" ")[1])
                showMealList(interactor.data)
                chooseMeal()

            }

            '8' -> {
                interactor.sortByName()
                showMealList(interactor.data)
                chooseMeal()
            }

            '9' -> {
                interactor.sortDescendingByName()
                showMealList(interactor.data)
                chooseMeal()
            }
            else -> {
                println("Incorrect input")
                showMealList(interactor.data)
                chooseMeal()
            }
        }
    }
}


suspend fun showReceipt(id: String) {
    val httpClient = ReceiptHttpClient(id)
    val gateway = ReceiptGatewayImplementation(httpClient)
    val interactor = ReceiptInteractor(gateway)
    val presenter = ReceiptPresenter(interactor)

    presenter.initialAction()

}