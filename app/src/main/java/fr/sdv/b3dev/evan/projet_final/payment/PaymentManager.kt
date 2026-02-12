package fr.sdv.b3dev.evan.projet_final.payment

import fr.sdv.b3dev.evan.projet_final.util.Logger
import kotlinx.coroutines.delay
import kotlin.random.Random

enum class PaymentMethod(val value: String, val label: String) {
    CARD("card", "Carte bancaire"),
    PAYPAL("paypal", "PayPal"),
    APPLE_PAY("apple_pay", "Apple Pay")
}

class PaymentManager(
    private val random: Random = Random.Default
) {

    suspend fun processPayment(
        method: PaymentMethod,
        amount: Double,
        cardNumber: String,
        cardHolder: String,
        expiryDate: String,
        cvv: String
    ): PaymentResult {
        if (amount <= 0.0) {
            return PaymentResult.Failure("Montant invalide")
        }

        if (method == PaymentMethod.CARD) {
            val cleanedNumber = cardNumber.filter { it.isDigit() }
            if (!isValidCardNumber(cleanedNumber)) {
                return PaymentResult.Failure("Numéro de carte invalide")
            }
            if (cardHolder.isBlank()) {
                return PaymentResult.Failure("Nom du titulaire requis")
            }
            if (!isValidExpiryDate(expiryDate)) {
                return PaymentResult.Failure("Date d'expiration invalide (MM/AA)")
            }
            if (!isValidCvv(cvv)) {
                return PaymentResult.Failure("CVV invalide")
            }
        }

        Logger.Payment.started(method.value, amount)
        delay(1800)

        return if (random.nextDouble() < 0.9) {
            PaymentResult.Processing
        } else {
            val reason = "Paiement refusé par l'émetteur"
            Logger.Payment.failed(reason)
            PaymentResult.Failure(reason)
        }
    }

    fun isValidCardNumber(cardNumber: String): Boolean {
        if (cardNumber.length !in 13..19 || !cardNumber.all { it.isDigit() }) return false

        var sum = 0
        var doubleDigit = false

        for (index in cardNumber.length - 1 downTo 0) {
            var digit = cardNumber[index].digitToInt()
            if (doubleDigit) {
                digit *= 2
                if (digit > 9) digit -= 9
            }
            sum += digit
            doubleDigit = !doubleDigit
        }

        return sum % 10 == 0
    }

    private fun isValidExpiryDate(expiryDate: String): Boolean {
        val regex = Regex("^(0[1-9]|1[0-2])/[0-9]{2}$")
        return regex.matches(expiryDate)
    }

    private fun isValidCvv(cvv: String): Boolean {
        val digits = cvv.filter { it.isDigit() }
        return digits.length in 3..4
    }
}
