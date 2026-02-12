package fr.sdv.b3dev.evan.projet_final.data.repository

import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.CartDao
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.CartItemWithSneaker
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.FavoriteDao
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.OrderDao
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.SneakerDao
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.UserDao
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.CartItemEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.FavoriteEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.OrderEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.UserEntity
import fr.sdv.b3dev.evan.projet_final.util.Logger
import kotlinx.coroutines.flow.Flow

class SneakerRepository(
    private val sneakerDao: SneakerDao,
    private val userDao: UserDao,
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
    private val favoriteDao: FavoriteDao
) {
    // Sneakers
    fun getAllSneakers(): Flow<List<SneakerEntity>> = sneakerDao.getAllSneakers()

    fun getSneakerById(id: Long): Flow<SneakerEntity?> = sneakerDao.getSneakerById(id)

    suspend fun getSneakerByIdOnce(id: Long): SneakerEntity? = sneakerDao.getSneakerByIdOnce(id)

    fun getUpcomingReleases(): Flow<List<SneakerEntity>> = sneakerDao.getUpcomingReleases()

    fun getAvailableSneakers(): Flow<List<SneakerEntity>> = sneakerDao.getAvailableSneakers()

    fun getSneakersByBrand(brand: String): Flow<List<SneakerEntity>> = sneakerDao.getSneakersByBrand(brand)

    fun searchSneakers(query: String): Flow<List<SneakerEntity>> {
        Logger.d("Repository", "Searching sneakers: $query")
        return sneakerDao.searchSneakers(query)
    }

    suspend fun getSneakerByBarcode(barcode: String): SneakerEntity? {
        Logger.Camera.barcodeScanned(barcode)
        return sneakerDao.getSneakerByBarcode(barcode)
    }

    fun getAllBrands(): Flow<List<String>> = sneakerDao.getAllBrands()

    // User
    fun getCurrentUser(): Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun getCurrentUserOnce(): UserEntity? = userDao.getCurrentUserOnce()

    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
        Logger.Database.update("User: ${user.displayName}")
    }

    suspend fun updateProfileImage(userId: Long, imagePath: String) {
        userDao.updateProfileImage(userId, imagePath)
        Logger.Camera.photoTaken(imagePath)
    }

    // Cart
    fun getCartItems(userId: Long): Flow<List<CartItemEntity>> = cartDao.getCartItems(userId)

    fun getCartItemsWithSneakers(userId: Long): Flow<List<CartItemWithSneaker>> =
        cartDao.getCartItemsWithSneakers(userId)

    fun getCartItemCount(userId: Long): Flow<Int> = cartDao.getCartItemCount(userId)

    fun getCartTotal(userId: Long): Flow<Double?> = cartDao.getCartTotal(userId)

    suspend fun addToCart(userId: Long, sneakerId: Long, size: String) {
        val existingItem = cartDao.getCartItem(userId, sneakerId, size)
        if (existingItem != null) {
            cartDao.incrementQuantity(existingItem.id)
            Logger.Cart.itemAdded(sneakerId, "$size (quantity+1)")
        } else {
            cartDao.insertCartItem(
                CartItemEntity(
                    sneakerId = sneakerId,
                    userId = userId,
                    size = size
                )
            )
            Logger.Cart.itemAdded(sneakerId, size)
        }
    }

    suspend fun removeFromCart(cartItemId: Long) {
        cartDao.deleteCartItemById(cartItemId)
        Logger.Cart.itemRemoved(cartItemId)
    }

    suspend fun updateCartItemQuantity(cartItem: CartItemEntity) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun clearCart(userId: Long) {
        cartDao.clearCart(userId)
        Logger.Cart.cleared()
    }

    // Favorites
    fun getFavoriteSneakers(userId: Long): Flow<List<SneakerEntity>> =
        favoriteDao.getFavoriteSneakers(userId)

    fun getFavoriteIds(userId: Long): Flow<List<Long>> = favoriteDao.getFavoriteIds(userId)

    fun isFavorite(userId: Long, sneakerId: Long): Flow<Boolean> =
        favoriteDao.isFavorite(userId, sneakerId)

    suspend fun toggleFavorite(userId: Long, sneakerId: Long) {
        val isFav = favoriteDao.isFavoriteOnce(userId, sneakerId)
        if (isFav) {
            favoriteDao.removeFavorite(userId, sneakerId)
            Logger.Favorites.removed(sneakerId)
        } else {
            favoriteDao.insertFavorite(FavoriteEntity(userId, sneakerId))
            Logger.Favorites.added(sneakerId)
        }
    }

    // Orders
    fun getOrdersByUser(userId: Long): Flow<List<OrderEntity>> = orderDao.getOrdersByUser(userId)

    fun getOrderById(orderId: Long): Flow<OrderEntity?> = orderDao.getOrderById(orderId)

    suspend fun createOrder(order: OrderEntity): Long {
        val orderId = orderDao.insertOrder(order)
        Logger.Database.insert("Order #$orderId created")
        return orderId
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        orderDao.updateOrderStatus(orderId, status)
        Logger.Database.update("Order #$orderId status: $status")
    }
}
