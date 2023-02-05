package receipt.domain

interface ReceiptGateway {
    suspend fun request() : ReceiptEntity
}