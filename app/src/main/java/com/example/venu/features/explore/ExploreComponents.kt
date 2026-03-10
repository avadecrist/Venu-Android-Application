package com.example.venu.features.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.venu.features.explore.model.PlaceUi

@Composable
fun PlaceCard(
    place: PlaceUi,
    selected: Boolean,
    onClick: () -> Unit,
    onToggleSaved: () -> Unit
) {
    val border = if (selected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = border,
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = place.name, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(text = place.subtitle, style = MaterialTheme.typography.bodyMedium)
                }

                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(text = "★ ${place.rating}", style = MaterialTheme.typography.bodyMedium)
                    Text(text = "${place.distanceKm} km", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Tag(label = place.genre.label)
                if (place.isVerified) Tag(label = "Verified")
                if (place.isSaved) Tag(label = "Saved")
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onToggleSaved,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(if (place.isSaved) "Unsave" else "Save")
            }
        }
    }
}

@Composable
private fun Tag(label: String) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}