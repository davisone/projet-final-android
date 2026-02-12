package fr.sdv.b3dev.evan.projet_final.ui.screens.checkout

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.CartItemWithSneaker
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.OrderEntity
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import fr.sdv.b3dev.evan.projet_final.payment.PaymentManager
import fr.sdv.b3dev.evan.projet_final.payment.PaymentMethod
import fr.sdv.b3dev.evan.projet_final.payment.PaymentResult
import fr.sdv.b3dev.evan.projet_final.util.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CheckoutViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)
    private val paymentManager = PaymentManager()

    private val userId = MutableStateFlow<Long?>(null)

    val cartItems: StateFlow<List<CartItemWithSneaker>> = userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getCartItemsWithSneakers(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotal: StateFlow<Double?> = userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getCartTotal(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _selectedMethod = MutableStateFlow(PaymentMethod.CARD)
    val selectedMethod: StateFlow<PaymentMethod> = _selectedMethod.asStateFlow()

    private val _cardHolder = MutableStateFlow("")
    val cardHolder: StateFlow<String> = _cardHolder.asStateFlow()

    private val _cardNumber = MutableStateFlow("")
    val cardNumber: StateFlow<String> = _cardNumber.asStateFlow()

    private val _expiryDate = MutableStateFlow("")
    val expiryDate: StateFlow<String> = _expiryDate.asStateFlow()

    private val _cvv = MutableStateFlow("")
    val cvv: StateFlow<String> = _cvv.asStateFlow()

    private val _shippingAddress = MutableStateFlow("")
    val shippingAddress: StateFlow<String> = _shippingAddress.asStateFlow()

    private val _paymentState = MutableStateFlow<PaymentResult>(PaymentResult.Idle)
    val paymentState: StateFlow<PaymentResult> = _paymentState.asStateFlow()

    init {
        viewModelScope.launch {
            userId.value = repository.getCurrentUserOnce()?.id ?: 1L
        }
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _selectedMethod.value = method
        _paymentState.value = PaymentResult.Idle
    }

    fun onCardHolderChange(value: String) {
        _cardHolder.value = value
    }

    fun onCardNumberChange(value: String) {
        _cardNumber.value = value
    }

    fun onExpiryDateChange(value: String) {
        _expiryDate.value = value
    }

    fun onCvvChange(value: String) {
        _cvv.value = value
    }

    fun onShippingAddressChange(value: String) {
        _shippingAddress.value = value
    }

    fun pay() {
        if (_paymentState.value is PaymentResult.Processing) return

        viewModelScope.launch {
            val currentUserId = userId.value
            if (currentUserId == null) {
                _paymentState.value = PaymentResult.Failure("Utilisateur introuvable")
                return@launch
            }

            val items = cartItems.value
            if (items.isEmpty()) {
                _paymentState.value = PaymentResult.Failure("Panier vide")
                return@launch
            }

            if (_shippingAddress.value.isBlank()) {
                _paymentState.value = PaymentResult.Failure("Adresse de livraison requise")
                return@launch
            }

            _paymentState.value = PaymentResult.Processing

            when (
                val paymentResult = paymentManager.processPayment(
                    method = _selectedMethod.value,
                    amount = cartTotal.value ?: 0.0,
                    cardNumber = _cardNumber.value,
                    cardHolder = _cardHolder.value,
                    expiryDate = _expiryDate.value,
                    cvv = _cvv.value
                )
            ) {
                is PaymentResult.Failure -> {
                    _paymentState.value = paymentResult
                }

                is PaymentResult.Processing -> {
                    try {
                        val orderId = repository.createOrder(
                            OrderEntity(
                                userId = currentUserId,
                                totalAmount = cartTotal.value ?: 0.0,
                                status = "confirmed",
                                paymentMethod = _selectedMethod.value.value,
                                items = itemsToJson(items),
                                shippingAddress = _shippingAddress.value
                            )
                        )
                        repository.clearCart(currentUserId)
                        Logger.Payment.success(orderId.toString())
                        _paymentState.value = PaymentResult.Success(orderId)
                    } catch (throwable: Throwable) {
                        Logger.Payment.failed("Erreur création commande")
                        _paymentState.value = PaymentResult.Failure("Erreur création commande")
                    }
                }

                else -> {
                    _paymentState.value = PaymentResult.Failure("Paiement non abouti")
                }
            }
        }
    }

    fun consumeSuccess() {
        if (_paymentState.value is PaymentResult.Success) {
            _paymentState.value = PaymentResult.Idle
        }
    }

    private fun itemsToJson(items: List<CartItemWithSneaker>): String {
        return items.joinToString(prefix = "[", postfix = "]") { item ->
            val name = item.sneaker.name.replace("\"", "")
            """{"sneakerId":${item.sneaker.id},"name":"$name","size":"${item.cartItem.size}","quantity":${item.cartItem.quantity},"price":${item.sneaker.price}}"""
        }
    }
}
