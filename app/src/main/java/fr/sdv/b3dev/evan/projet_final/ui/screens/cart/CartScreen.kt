package fr.sdv.b3dev.evan.projet_final.ui.screens.cart

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CartScreen(
    onGoToCheckout: () -> Unit,
    onSneakerClick: (Long) -> Unit,
    viewModel: CartViewModel = viewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val total by viewModel.cartTotal.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Mon panier", style = MaterialTheme.typography.headlineSmall)
        }

        if (cartItems.isEmpty()) {
            item { Text("Ton panier est vide") }
        } else {
            items(cartItems, key = { it.cartItem.id }) { item ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.sneaker.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Taille ${item.cartItem.size} • ${item.sneaker.price} €",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(onClick = { viewModel.decreaseQuantity(item) }) {
                                Text("-")
                            }
                            Text(
                                text = item.cartItem.quantity.toString(),
                                modifier = Modifier.padding(top = 10.dp)
                            )
                            OutlinedButton(onClick = { viewModel.increaseQuantity(item) }) {
                                Text("+")
                            }
                        }

                        OutlinedButton(onClick = { viewModel.removeItem(item.cartItem.id) }) {
                            Text("Supprimer")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { onSneakerClick(item.sneaker.id) }) {
                        Text("Voir produit")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total: ${"%.2f".format(total ?: 0.0)} €",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = viewModel::clearCart, modifier = Modifier.weight(1f)) {
                        Text("Vider")
                    }
                    Button(onClick = onGoToCheckout, modifier = Modifier.weight(1f)) {
                        Text("Payer")
                    }
                }
            }
        }
    }
}
