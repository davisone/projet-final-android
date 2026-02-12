package fr.sdv.b3dev.evan.projet_final.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(releaseDate: Long) {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(releaseDate) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }

    val remaining = releaseDate - now
    if (remaining <= 0L) {
        Text(
            text = "Disponible maintenant",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        return
    }

    val days = remaining / (24 * 60 * 60 * 1000)
    val hours = (remaining / (60 * 60 * 1000)) % 24
    val minutes = (remaining / (60 * 1000)) % 60

    Text(
        text = "Sortie dans ${days}j ${hours}h ${minutes}min",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.tertiary
    )
}
