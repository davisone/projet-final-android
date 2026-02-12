package fr.sdv.b3dev.evan.projet_final.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SneakerDao {

    @Query("SELECT * FROM sneakers ORDER BY releaseDate DESC")
    fun getAllSneakers(): Flow<List<SneakerEntity>>

    @Query("SELECT * FROM sneakers WHERE id = :id")
    fun getSneakerById(id: Long): Flow<SneakerEntity?>

    @Query("SELECT * FROM sneakers WHERE id = :id")
    suspend fun getSneakerByIdOnce(id: Long): SneakerEntity?

    @Query("SELECT * FROM sneakers WHERE isUpcoming = 1 ORDER BY releaseDate ASC")
    fun getUpcomingReleases(): Flow<List<SneakerEntity>>

    @Query("SELECT * FROM sneakers WHERE isUpcoming = 0 ORDER BY releaseDate DESC")
    fun getAvailableSneakers(): Flow<List<SneakerEntity>>

    @Query("SELECT * FROM sneakers WHERE brand = :brand ORDER BY releaseDate DESC")
    fun getSneakersByBrand(brand: String): Flow<List<SneakerEntity>>

    @Query("SELECT * FROM sneakers WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%' OR colorway LIKE '%' || :query || '%'")
    fun searchSneakers(query: String): Flow<List<SneakerEntity>>

    @Query("SELECT * FROM sneakers WHERE barcode = :barcode LIMIT 1")
    suspend fun getSneakerByBarcode(barcode: String): SneakerEntity?

    @Query("SELECT DISTINCT brand FROM sneakers ORDER BY brand ASC")
    fun getAllBrands(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSneaker(sneaker: SneakerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSneakers(sneakers: List<SneakerEntity>)

    @Update
    suspend fun updateSneaker(sneaker: SneakerEntity)

    @Delete
    suspend fun deleteSneaker(sneaker: SneakerEntity)

    @Query("DELETE FROM sneakers")
    suspend fun deleteAllSneakers()
}
