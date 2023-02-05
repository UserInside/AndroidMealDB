package receipt.domain

class ReceiptInteractor(
    private val gateway: ReceiptGateway
) {

    suspend fun fetchReceipt() : ReceiptEntity {
        return gateway.request()
    }
}