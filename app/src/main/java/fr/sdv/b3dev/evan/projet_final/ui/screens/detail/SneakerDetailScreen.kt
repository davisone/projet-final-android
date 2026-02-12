package fr.sdv.b3dev.evan.projet_final.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import fr.sdv.b3dev.evan.projet_final.ui.components.CountdownTimer

@Composable
fun SneakerDetailScreen(
    sneakerId: Long,
    onGoToCart: () -> Unit,
    viewModel: SneakerDetailViewModel = viewModel()
) {
    val sneaker by viewModel.sneaker.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    var selectedSize by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(sneakerId) {
        viewModel.loadSneaker(sneakerId)
    }

    val currentSneaker = sneaker
    if (currentSneaker == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Sneaker introuvable")
        }
        return
    }

    val sizes = currentSneaker.sizes
        .split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            AsyncImage(
                model = currentSneaker.imageUrl,
                contentDescription = currentSneaker.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Text(
                text = currentSneaker.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${currentSneaker.brand} • ${currentSneaker.colorway}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${currentSneaker.price} €",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (currentSneaker.isUpcoming) {
            item {
                CountdownTimer(releaseDate = currentSneaker.releaseDate)
            }
        }

        item {
            Text(
                text = currentSneaker.description,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Text("Tailles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sizes) { size ->
                    val selected = selectedSize == size
                    if (selected) {
                        Button(onClick = { selectedSize = size }) {
                            Text(size)
                        }
                    } else {
                        OutlinedButton(onClick = { selectedSize = size }) {
                            Text(size)
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = viewModel::toggleFavorite,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isFavorite) "Retirer favori" else "Ajouter favori")
                }

                Button(
                    onClick = {
                        val size = selectedSize ?: sizes.firstOrNull() ?: "42"
                        viewModel.addToCart(size)
                        onGoToCart()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ajouter au panier")
                }
            }
        }
    }
}
