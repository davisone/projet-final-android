package fr.sdv.b3dev.evan.projet_final.ui.screens.releases

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReleasesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)

    val upcomingReleases: StateFlow<List<SneakerEntity>> = repository
        .getUpcomingReleases()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
