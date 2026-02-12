package fr.sdv.b3dev.evan.projet_final.ui.screens.releases

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import fr.sdv.b3dev.evan.projet_final.ui.components.CountdownTimer
import fr.sdv.b3dev.evan.projet_final.ui.components.SneakerCard

@Composable
fun ReleasesScreen(
    onSneakerClick: (Long) -> Unit,
    viewModel: ReleasesViewModel = viewModel()
) {
    val releases by viewModel.upcomingReleases.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Prochaines sorties",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        items(releases, key = { it.id }) { sneaker ->
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
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
