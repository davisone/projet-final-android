package fr.sdv.b3dev.evan.projet_final.payment

sealed class PaymentResult {
    data object Idle : PaymentResult()
    data object Processing : PaymentResult()
    data class Success(val orderId: Long) : PaymentResult()
    data class Failure(val reason: String) : PaymentResult()
}
