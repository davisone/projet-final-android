package fr.sdv.b3dev.evan.projet_final.ui.screens.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.data.local.database.entity.SneakerEntity
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SneakerDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)

    private val _sneaker = MutableStateFlow<SneakerEntity?>(null)
    val sneaker: StateFlow<SneakerEntity?> = _sneaker.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private var currentSneakerId: Long? = null
    private var sneakerJob: Job? = null
    private var favoriteJob: Job? = null

    fun loadSneaker(sneakerId: Long) {
        if (currentSneakerId == sneakerId) return
        currentSneakerId = sneakerId

        sneakerJob?.cancel()
        favoriteJob?.cancel()

        sneakerJob = viewModelScope.launch {
            repository.getSneakerById(sneakerId).collect { _sneaker.value = it }
        }

        favoriteJob = viewModelScope.launch {
            val userId = getCurrentUserId()
            repository.isFavorite(userId, sneakerId).collect { _isFavorite.value = it }
        }
    }

    fun toggleFavorite() {
        val sneakerId = currentSneakerId ?: return
        viewModelScope.launch {
            repository.toggleFavorite(getCurrentUserId(), sneakerId)
        }
    }

    fun addToCart(size: String) {
        val sneakerId = currentSneakerId ?: return
        viewModelScope.launch {
            repository.addToCart(
                userId = getCurrentUserId(),
                sneakerId = sneakerId,
                size = size
            )
        }
    }

    private suspend fun getCurrentUserId(): Long {
        return repository.getCurrentUserOnce()?.id ?: 1L
    }
}
