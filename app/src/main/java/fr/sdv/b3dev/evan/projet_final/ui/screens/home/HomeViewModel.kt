package fr.sdv.b3dev.evan.projet_final.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)

    val featuredSneakers: StateFlow<List<SneakerEntity>> = repository
        .getAvailableSneakers()
        .map { sneakers -> sneakers.take(6) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val upcomingSneakers: StateFlow<List<SneakerEntity>> = repository
        .getUpcomingReleases()
        .map { sneakers -> sneakers.take(4) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val brands: StateFlow<List<String>> = repository
        .getAllBrands()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
