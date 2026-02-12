package fr.sdv.b3dev.evan.projet_final.ui.screens.search

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.evan.projet_final.audio.VoiceSearchManager
import fr.sdv.b3dev.evan.projet_final.ui.components.SearchBar
import fr.sdv.b3dev.evan.projet_final.ui.components.SneakerCard
import fr.sdv.b3dev.evan.projet_final.util.Logger

@Composable
fun SearchScreen(
    onSneakerClick: (Long) -> Unit,
    onOpenScanner: () -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val context = LocalContext.current
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()

    val voiceManager = remember { VoiceSearchManager(context) }
    var isListening by remember { mutableStateOf(false) }
    var voiceMessage by remember { mutableStateOf<String?>(null) }
    var requestStartVoice by remember { mutableStateOf(false) }
    var hasAudioPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
        )
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasAudioPermission = granted
        if (!granted) {
            voiceMessage = "Permission micro refusée"
        }
    }

    LaunchedEffect(requestStartVoice, hasAudioPermission) {
        if (!requestStartVoice) return@LaunchedEffect

        if (!hasAudioPermission) {
            requestStartVoice = false
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return@LaunchedEffect
        }

        requestStartVoice = false
        voiceManager.startListening(
            onResult = { text ->
                viewModel.onQueryChange(text)
                voiceMessage = "Recherche vocale: $text"
            },
            onError = { message ->
                voiceMessage = message
            },
            onReady = {
                isListening = true
                voiceMessage = "Écoute en cours..."
            },
            onEnd = {
                isListening = false
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            voiceManager.stopListening()
            voiceManager.release()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Recherche",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            SearchBar(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onVoiceSearchClick = {
                    Logger.VoiceSearch.started()
                    requestStartVoice = true
                }
            )
        }

        if (!voiceMessage.isNullOrBlank()) {
            item {
                Text(
                    text = if (isListening) "Écoute..." else voiceMessage.orEmpty(),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Button(
                onClick = onOpenScanner,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text("Scanner un code-barres")
            }
        }

        if (results.isEmpty()) {
            item {
                Text(
                    text = "Aucun resultat",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        } else {
            items(results, key = { it.id }) { sneaker ->
                SneakerCard(
                    sneaker = sneaker,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = { onSneakerClick(sneaker.id) }
                )
            }
        }
    }
}
