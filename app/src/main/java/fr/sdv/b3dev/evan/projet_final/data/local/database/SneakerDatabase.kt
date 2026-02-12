package fr.sdv.b3dev.evan.projet_final.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import fr.sdv.b3dev.evan.projet_final.data.SampleData
import fr.sdv.b3dev.evan.projet_final.data.local.database.dao.CartDao
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        SneakerEntity::class,
        UserEntity::class,
        CartItemEntity::class,
        OrderEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SneakerDatabase : RoomDatabase() {

    abstract fun sneakerDao(): SneakerDao
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: SneakerDatabase? = null

        fun getDatabase(context: Context): SneakerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SneakerDatabase::class.java,
                    "sneaker_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                Logger.Database.query("Database initialized")
                instance
            }
        }
    }

    private class DatabaseCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Logger.Database.query("Database created, inserting sample data")
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database)
                }
            }
        }

        suspend fun populateDatabase(database: SneakerDatabase) {
            // Insert default user
            val userId = database.userDao().insertUser(SampleData.defaultUser)
            Logger.Database.insert("Default user created with id: $userId")

            // Insert sample sneakers
            database.sneakerDao().insertSneakers(SampleData.sampleSneakers)
            Logger.Database.insert("${SampleData.sampleSneakers.size} sample sneakers inserted")
        }
    }
}
