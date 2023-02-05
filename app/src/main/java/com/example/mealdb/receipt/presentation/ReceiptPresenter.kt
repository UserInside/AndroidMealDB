package receipt.presentation

import receipt.domain.ReceiptEntity
import receipt.domain.ReceiptInteractor

class ReceiptPresenter(
    private val interactor: ReceiptInteractor
) {
    var data : ReceiptEntity? = null

    suspend fun initialAction() {
        data = interactor.fetchReceipt()
        println(data)
    }
}