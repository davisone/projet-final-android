package fr.sdv.b3dev.evan.projet_final.ui.screens.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.evan.projet_final.camera.CameraManager
import fr.sdv.b3dev.evan.projet_final.camera.CameraMode
import fr.sdv.b3dev.evan.projet_final.util.Logger

@Composable
fun CameraScreen(
    modeArg: String?,
    onClose: () -> Unit,
    onSneakerFound: (Long) -> Unit,
    viewModel: CameraViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mode by viewModel.mode.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()
    val foundSneakerId by viewModel.foundSneakerId.collectAsState()
    val capturedPhotoPath by viewModel.capturedPhotoPath.collectAsState()

    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    val cameraManager = remember { CameraManager(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(modeArg) {
        viewModel.setMode(CameraMode.fromRoute(modeArg))
    }

    LaunchedEffect(foundSneakerId) {
        val sneakerId = foundSneakerId ?: return@LaunchedEffect
        onSneakerFound(sneakerId)
        viewModel.consumeFoundSneaker()
    }

    DisposableEffect(Unit) {
        onDispose { cameraManager.release() }
    }

    LaunchedEffect(previewView, hasCameraPermission, mode) {
        val currentPreview = previewView ?: return@LaunchedEffect
        if (!hasCameraPermission) return@LaunchedEffect

        cameraManager.startCamera(
            lifecycleOwner = lifecycleOwner,
            previewView = currentPreview,
            enableBarcodeScanner = mode == CameraMode.SCAN,
            onBarcodeDetected = viewModel::onBarcodeDetected,
            onError = { throwable ->
                Logger.Camera.error("Unable to start camera", throwable)
            }
        )
    }

    if (!hasCameraPermission) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Permission caméra requise")
                Button(
                    modifier = Modifier.padding(top = 12.dp),
                    onClick = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                ) {
                    Text("Autoriser")
                }
                OutlinedButton(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = onClose
                ) {
                    Text("Fermer")
                }
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).also { previewView = it }
            },
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(Color.Black.copy(alpha = 0.45f))
                .padding(12.dp)
        ) {
            Text(
                text = if (mode == CameraMode.SCAN) "Scanner code-barres" else "Photo profil",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
            if (!statusMessage.isNullOrBlank()) {
                Text(
                    text = statusMessage.orEmpty(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (!capturedPhotoPath.isNullOrBlank() && mode == CameraMode.PROFILE) {
                Text(
                    text = "Enregistrée: ${capturedPhotoPath}",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.45f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (mode == CameraMode.PROFILE) {
                Button(
                    onClick = {
                        cameraManager.takePhoto(
                            onPhotoSaved = viewModel::onPhotoCaptured,
                            onError = { throwable ->
                                Logger.Camera.error("Unable to take profile photo", throwable)
                            }
                        )
                    }
                ) {
                    Text("Prendre photo")
                }
            }

            OutlinedButton(
                modifier = Modifier.padding(top = 8.dp),
                onClick = onClose
            ) {
                Text("Retour")
            }
        }
    }
}
