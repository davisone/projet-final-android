package fr.sdv.b3dev.evan.projet_final.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    onGoToCart: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val user by viewModel.user.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val totalSpent by viewModel.totalSpent.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Profil", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = user?.displayName ?: "Utilisateur",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user?.email ?: "Aucun email",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Depense totale: ${"%.2f".format(totalSpent)} €",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(onClick = onGoToCart, modifier = Modifier.padding(top = 8.dp)) {
                    Text("Voir le panier")
                }
            }
        }

        item {
            Text("Commandes", style = MaterialTheme.typography.titleMedium)
        }

        if (orders.isEmpty()) {
            item {
                Text("Aucune commande pour l'instant")
            }
        } else {
            items(orders, key = { it.id }) { order ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        text = "Commande #${order.id}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Statut: ${order.status}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Total: ${"%.2f".format(order.totalAmount)} €",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
