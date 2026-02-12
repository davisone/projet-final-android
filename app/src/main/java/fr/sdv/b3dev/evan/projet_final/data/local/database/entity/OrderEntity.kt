package fr.sdv.b3dev.evan.projet_final.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val totalAmount: Double,
    val status: String, // "pending", "confirmed", "shipped", "delivered", "cancelled"
    val paymentMethod: String, // "card", "paypal", "apple_pay"
    val items: String, // JSON string des items commandés
    val shippingAddress: String,
    val createdAt: Long = System.currentTimeMillis()
)
