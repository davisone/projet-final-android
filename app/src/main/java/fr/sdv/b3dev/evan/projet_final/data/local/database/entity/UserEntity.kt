package fr.sdv.b3dev.evan.projet_final.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val displayName: String,
    val profileImagePath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
