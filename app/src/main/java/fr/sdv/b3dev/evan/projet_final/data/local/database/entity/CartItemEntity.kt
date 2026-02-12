package fr.sdv.b3dev.evan.projet_final.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = SneakerEntity::class,
            parentColumns = ["id"],
            childColumns = ["sneakerId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sneakerId"]),
        Index(value = ["userId"])
    ]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sneakerId: Long,
    val userId: Long,
    val size: String,
    val quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
)
