package fr.sdv.b3dev.evan.projet_final.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "sneakerId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SneakerEntity::class,
            parentColumns = ["id"],
            childColumns = ["sneakerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sneakerId"]),
        Index(value = ["userId"])
    ]
)
data class FavoriteEntity(
    val userId: Long,
    val sneakerId: Long,
    val addedAt: Long = System.currentTimeMillis()
)
