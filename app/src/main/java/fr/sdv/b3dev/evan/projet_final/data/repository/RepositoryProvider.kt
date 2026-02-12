package fr.sdv.b3dev.evan.projet_final.data.repository

import android.content.Context
import fr.sdv.b3dev.evan.projet_final.data.local.database.SneakerDatabase

object RepositoryProvider {

    @Volatile
    private var instance: SneakerRepository? = null

    fun getRepository(context: Context): SneakerRepository {
        return instance ?: synchronized(this) {
            instance ?: createRepository(context.applicationContext).also { instance = it }
        }
    }

    private fun createRepository(context: Context): SneakerRepository {
        val database = SneakerDatabase.getDatabase(context)
        return SneakerRepository(
            sneakerDao = database.sneakerDao(),
            userDao = database.userDao(),
            cartDao = database.cartDao(),
            orderDao = database.orderDao(),
            favoriteDao = database.favoriteDao()
        )
    }
}
