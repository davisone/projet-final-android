package fr.sdv.b3dev.evan.projet_final.ui.screens.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.CartItemWithSneaker
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)

    private val userId = MutableStateFlow<Long?>(null)

    val cartItems: StateFlow<List<CartItemWithSneaker>> = userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getCartItemsWithSneakers(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartTotal: StateFlow<Double?> = userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getCartTotal(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    init {
        viewModelScope.launch {
            userId.value = repository.getCurrentUserOnce()?.id ?: 1L
        }
    }

    fun increaseQuantity(item: CartItemWithSneaker) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(
                item.cartItem.copy(quantity = item.cartItem.quantity + 1)
            )
        }
    }

    fun decreaseQuantity(item: CartItemWithSneaker) {
        if (item.cartItem.quantity <= 1) {
            removeItem(item.cartItem.id)
            return
        }
        viewModelScope.launch {
            repository.updateCartItemQuantity(
                item.cartItem.copy(quantity = item.cartItem.quantity - 1)
            )
        }
    }

    fun removeItem(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun clearCart() {
        val current = userId.value ?: return
        viewModelScope.launch {
            repository.clearCart(current)
        }
    }
}
