package fr.sdv.b3dev.evan.projet_final.ui.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.evan.projet_final.ui.components.SneakerCard

@Composable
fun FavoritesScreen(
    onSneakerClick: (Long) -> Unit,
    viewModel: FavoritesViewModel = viewModel()
) {
    val favorites by viewModel.favorites.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Mes favoris",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        if (favorites.isEmpty()) {
            item {
                Text("Aucun favori pour le moment")
            }
        } else {
            items(favorites, key = { it.id }) { sneaker ->
                SneakerCard(
                    sneaker = sneaker,
                    isFavorite = true,
                    onFavoriteClick = { viewModel.toggleFavorite(sneaker.id) },
                    onClick = { onSneakerClick(sneaker.id) }
                )
            }
        }
    }
}
