package fr.sdv.b3dev.evan.projet_final.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sneakers")
data class SneakerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val brand: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val releaseDate: Long, // Timestamp
    val isUpcoming: Boolean = false,
    val colorway: String,
    val sizes: String, // Sizes séparées par virgule: "38,39,40,41,42,43,44,45"
    val barcode: String? = null // Pour le scanner
)
