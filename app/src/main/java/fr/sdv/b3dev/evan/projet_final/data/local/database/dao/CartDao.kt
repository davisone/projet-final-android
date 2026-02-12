package fr.sdv.b3dev.evan.projet_final.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.CartItemEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import kotlinx.coroutines.flow.Flow

data class CartItemWithSneaker(
    val cartItem: CartItemEntity,
    val sneaker: SneakerEntity
)

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY addedAt DESC")
    fun getCartItems(userId: Long): Flow<List<CartItemEntity>>

    @Query("""
        SELECT c.*, s.* FROM cart_items c
        INNER JOIN sneakers s ON c.sneakerId = s.id
        WHERE c.userId = :userId
        ORDER BY c.addedAt DESC
    """)
    fun getCartItemsWithSneakers(userId: Long): Flow<List<CartItemWithSneaker>>

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND sneakerId = :sneakerId AND size = :size LIMIT 1")
    suspend fun getCartItem(userId: Long, sneakerId: Long, size: String): CartItemEntity?

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    fun getCartItemCount(userId: Long): Flow<Int>

    @Query("SELECT SUM(c.quantity * s.price) FROM cart_items c INNER JOIN sneakers s ON c.sneakerId = s.id WHERE c.userId = :userId")
    fun getCartTotal(userId: Long): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity): Long

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE id = :cartItemId")
    suspend fun incrementQuantity(cartItemId: Long)

    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE id = :cartItemId AND quantity > 1")
    suspend fun decrementQuantity(cartItemId: Long)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE id = :cartItemId")
    suspend fun deleteCartItemById(cartItemId: Long)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Long)
}
