package fr.sdv.b3dev.evan.projet_final

import android.app.Application
import android.content.pm.ApplicationInfo
import fr.sdv.b3dev.evan.projet_final.util.Logger
import timber.log.Timber

class SneakerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialisation de Timber pour le logging
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebuggable) {
            Timber.plant(Timber.DebugTree())
        }

        Logger.i("App", "SneakerApp initialized")
    }
}
