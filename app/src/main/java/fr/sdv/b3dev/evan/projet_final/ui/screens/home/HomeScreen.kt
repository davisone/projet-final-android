package fr.sdv.b3dev.evan.projet_final.ui.screens.home

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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.evan.projet_final.ui.components.CountdownTimer
import fr.sdv.b3dev.evan.projet_final.ui.components.SneakerCard

@Composable
fun HomeScreen(
    onSneakerClick: (Long) -> Unit,
    onGoToReleases: () -> Unit,
    onGoToCart: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val featuredSneakers by viewModel.featuredSneakers.collectAsState()
    val upcomingSneakers by viewModel.upcomingSneakers.collectAsState()
    val brands by viewModel.brands.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SNKRS Store",
                    style = MaterialTheme.typography.headlineSmall
                )
                Button(onClick = onGoToCart) {
                    Text("Panier")
                }
            }
        }

        item {
            Text("Marques", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(brands) { brand ->
                    AssistChip(onClick = {}, label = { Text(brand) })
                }
            }
        }

        item {
            Text("En vedette", style = MaterialTheme.typography.titleMedium)
        }

        items(featuredSneakers, key = { it.id }) { sneaker ->
            SneakerCard(
                sneaker = sneaker,
                onClick = { onSneakerClick(sneaker.id) }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Prochaines sorties", style = MaterialTheme.typography.titleMedium)
                Button(onClick = onGoToReleases) {
                    Text("Voir tout")
                }
            }
        }

        items(upcomingSneakers, key = { it.id }) { sneaker ->
            Column {
                SneakerCard(
                    sneaker = sneaker,
                    onClick = { onSneakerClick(sneaker.id) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                CountdownTimer(releaseDate = sneaker.releaseDate)
            }
        }
    }
}
