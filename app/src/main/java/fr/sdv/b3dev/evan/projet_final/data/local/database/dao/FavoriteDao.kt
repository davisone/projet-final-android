package fr.sdv.b3dev.evan.projet_final.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.FavoriteEntity
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("""
        SELECT s.* FROM favorites f
        INNER JOIN sneakers s ON f.sneakerId = s.id
        WHERE f.userId = :userId
        ORDER BY f.addedAt DESC
    """)
    fun getFavoriteSneakers(userId: Long): Flow<List<SneakerEntity>>

    @Query("SELECT sneakerId FROM favorites WHERE userId = :userId")
    fun getFavoriteIds(userId: Long): Flow<List<Long>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND sneakerId = :sneakerId)")
    fun isFavorite(userId: Long, sneakerId: Long): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND sneakerId = :sneakerId)")
    suspend fun isFavoriteOnce(userId: Long, sneakerId: Long): Boolean

    @Query("SELECT COUNT(*) FROM favorites WHERE userId = :userId")
    fun getFavoriteCount(userId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE userId = :userId AND sneakerId = :sneakerId")
    suspend fun removeFavorite(userId: Long, sneakerId: Long)

    @Query("DELETE FROM favorites WHERE userId = :userId")
    suspend fun clearFavorites(userId: Long)
}
