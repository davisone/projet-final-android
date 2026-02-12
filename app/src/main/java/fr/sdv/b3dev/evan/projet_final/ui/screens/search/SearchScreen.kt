package fr.sdv.b3dev.evan.projet_final.ui.screens.search

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.evan.projet_final.ui.components.SearchBar
import fr.sdv.b3dev.evan.projet_final.ui.components.SneakerCard
import fr.sdv.b3dev.evan.projet_final.util.Logger

@Composable
fun SearchScreen(
    onSneakerClick: (Long) -> Unit,
    onOpenScanner: () -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()

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
                }
            )
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
