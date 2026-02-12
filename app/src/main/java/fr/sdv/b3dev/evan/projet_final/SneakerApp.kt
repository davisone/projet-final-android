package fr.sdv.b3dev.evan.projet_final

import android.app.Application
import fr.sdv.b3dev.evan.projet_final.util.Logger
import timber.log.Timber

class SneakerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialisation de Timber pour le logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Logger.i("App", "SneakerApp initialized")
    }
}
