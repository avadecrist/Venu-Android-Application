package com.example.venu.core.core_common.eventdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors

@Composable
fun DetailInfoCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    accentChip: String? = null
) {
    Surface(
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VenuColors.TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = VenuColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (accentChip != null) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = VenuColors.GenreChipBg
                ) {
                    Text(
                        text = accentChip,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = VenuColors.GenreChipText,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = VenuColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}