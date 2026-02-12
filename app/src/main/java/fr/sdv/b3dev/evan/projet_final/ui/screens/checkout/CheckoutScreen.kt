package fr.sdv.b3dev.evan.projet_final.ui.screens.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.sdv.b3dev.evan.projet_final.payment.PaymentMethod
import fr.sdv.b3dev.evan.projet_final.payment.PaymentResult

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onPaymentSuccess: (Long) -> Unit,
    viewModel: CheckoutViewModel = viewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val cartTotal by viewModel.cartTotal.collectAsState()
    val selectedMethod by viewModel.selectedMethod.collectAsState()
    val cardHolder by viewModel.cardHolder.collectAsState()
    val cardNumber by viewModel.cardNumber.collectAsState()
    val expiryDate by viewModel.expiryDate.collectAsState()
    val cvv by viewModel.cvv.collectAsState()
    val shippingAddress by viewModel.shippingAddress.collectAsState()
    val paymentState by viewModel.paymentState.collectAsState()

    LaunchedEffect(paymentState) {
        val state = paymentState
        if (state is PaymentResult.Success) {
            onPaymentSuccess(state.orderId)
            viewModel.consumeSuccess()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Checkout", style = MaterialTheme.typography.headlineSmall)
        }

        item {
            Text("Méthode de paiement", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentMethod.entries.forEach { method ->
                    if (method == selectedMethod) {
                        Button(
                            onClick = { viewModel.setPaymentMethod(method) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(method.label)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.setPaymentMethod(method) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(method.label)
                        }
                    }
                }
            }
        }

        if (selectedMethod == PaymentMethod.CARD) {
            item {
                OutlinedTextField(
                    value = cardHolder,
                    onValueChange = viewModel::onCardHolderChange,
                    label = { Text("Nom du titulaire") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = viewModel::onCardNumberChange,
                    label = { Text("Numéro de carte") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = viewModel::onExpiryDateChange,
                        label = { Text("MM/AA") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = viewModel::onCvvChange,
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = shippingAddress,
                onValueChange = viewModel::onShippingAddressChange,
                label = { Text("Adresse de livraison") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
        }

        item {
            Text("Récapitulatif", style = MaterialTheme.typography.titleMedium)
        }

        if (cartItems.isEmpty()) {
            item {
                Text("Panier vide")
            }
        } else {
            items(cartItems, key = { it.cartItem.id }) { item ->
                Text(
                    text = "• ${item.sneaker.name} x${item.cartItem.quantity} (${item.cartItem.size})",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Text(
                text = "Total: ${"%.2f".format(cartTotal ?: 0.0)} €",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            when (val state = paymentState) {
                is PaymentResult.Failure -> {
                    Text(
                        text = state.reason,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                PaymentResult.Processing -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Text("Paiement en cours...")
                    }
                }

                else -> Unit
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Retour")
                }
                Button(
                    onClick = viewModel::pay,
                    enabled = paymentState !is PaymentResult.Processing && cartItems.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Confirmer paiement")
                }
            }
        }
    }
}
