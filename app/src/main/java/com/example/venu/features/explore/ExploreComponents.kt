package com.example.venu.features.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.venu.core.core_common.util.formatDistance
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.components.BaseEventCard
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_common.eventdetails.genreChipText
import com.example.venu.core.core_domain.model.Genre
import com.example.venu.core.core_domain.model.label
import com.example.venu.core.core_presentation.genreColor
import com.example.venu.features.explore.model.PlaceUi

@Composable
fun PlaceCard(
    place: PlaceUi,
    selected: Boolean,
    onClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    BaseEventCard(
        modifier = Modifier.fillMaxWidth(),
        selected = selected,
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(text = place.subtitle, style = MaterialTheme.typography.bodyMedium)
                }

                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(text = "★ ${place.rating}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = place.distanceKm?.let { formatDistance(it) } ?: "Distance unavailable",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GenreTag(genre = place.genre)
                if (place.isVerified) {
                    Tag(
                        label = "Verified",
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            if (place.savedLabel != null) {
                FilledTonalButton(
                    onClick = onSaveClick,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Saved • ${place.savedLabel}")
                }
            }
        }
    }
}


@Composable
fun Tag(
    label: String,
    color: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Surface(
        color = color,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun GenreTag(genre: Genre) {
    val baseColor = genreColor(genre)

    Tag(
        label = genreChipText(genre),
        color = baseColor.copy(alpha = 0.16f),
        contentColor = baseColor
    )
}