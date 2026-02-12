package fr.sdv.b3dev.evan.projet_final.ui.screens.camera

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fr.sdv.b3dev.evan.projet_final.camera.CameraMode
import fr.sdv.b3dev.evan.projet_final.data.repository.RepositoryProvider
import fr.sdv.b3dev.evan.projet_final.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RepositoryProvider.getRepository(application)

    private val _mode = MutableStateFlow(CameraMode.SCAN)
    val mode: StateFlow<CameraMode> = _mode.asStateFlow()

    private val _scanResult = MutableStateFlow<String?>(null)
    val scanResult: StateFlow<String?> = _scanResult.asStateFlow()

    private val _foundSneakerId = MutableStateFlow<Long?>(null)
    val foundSneakerId: StateFlow<Long?> = _foundSneakerId.asStateFlow()

    private val _capturedPhotoPath = MutableStateFlow<String?>(null)
    val capturedPhotoPath: StateFlow<String?> = _capturedPhotoPath.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private var searchingBarcode = false

    fun setMode(mode: CameraMode) {
        _mode.value = mode
        _statusMessage.value = if (mode == CameraMode.SCAN) {
            "Scanne un code-barres"
        } else {
            "Prends une photo de profil"
        }
    }

    fun onBarcodeDetected(rawValue: String) {
        if (_mode.value != CameraMode.SCAN || searchingBarcode) return

        searchingBarcode = true
        _scanResult.value = rawValue
        Logger.Camera.barcodeScanned(rawValue)

        viewModelScope.launch {
            val sneaker = repository.getSneakerByBarcode(rawValue)
            if (sneaker != null) {
                _statusMessage.value = "Sneaker trouvée: ${sneaker.name}"
                _foundSneakerId.value = sneaker.id
            } else {
                _statusMessage.value = "Aucune sneaker trouvée pour ce code-barres"
            }
            searchingBarcode = false
        }
    }

    fun onPhotoCaptured(path: String) {
        _capturedPhotoPath.value = path
        viewModelScope.launch {
            val userId = repository.getCurrentUserOnce()?.id ?: 1L
            repository.updateProfileImage(userId, path)
            _statusMessage.value = "Photo de profil mise à jour"
        }
    }

    fun consumeFoundSneaker() {
        _foundSneakerId.value = null
    }

    fun clearStatus() {
        _statusMessage.value = null
    }
}
