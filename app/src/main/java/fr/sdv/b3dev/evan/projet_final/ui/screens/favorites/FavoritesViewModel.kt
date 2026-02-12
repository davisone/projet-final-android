package fr.sdv.b3dev.evan.projet_final.ui.screens.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)

    private val userId = MutableStateFlow<Long?>(null)

    val favorites: StateFlow<List<SneakerEntity>> = userId
        .filterNotNull()
        .flatMapLatest { id -> repository.getFavoriteSneakers(id) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            userId.value = repository.getCurrentUserOnce()?.id ?: 1L
        }
    }

    fun toggleFavorite(sneakerId: Long) {
        val current = userId.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(current, sneakerId)
        }
    }
}
