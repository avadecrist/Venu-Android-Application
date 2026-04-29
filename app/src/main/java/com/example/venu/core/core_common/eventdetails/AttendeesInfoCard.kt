package com.example.venu.core.core_common.eventdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_domain.model.CrowdLevel

@Composable
fun AttendeesInfoCard(
    attendeeCount: Int,
    crowdLevel: CrowdLevel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Group,
                    contentDescription = null,
                    tint = VenuColors.TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Attendees",
                    style = MaterialTheme.typography.titleMedium,
                    color = VenuColors.TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            CrowdLevelIndicator(crowdLevel = crowdLevel)

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = crowdLevelLabel(crowdLevel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = VenuColors.TextMuted,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VenuColors.TextMuted.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "$attendeeCount going",
                    style = MaterialTheme.typography.bodySmall,
                    color = VenuColors.TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CrowdLevelIndicator(
    crowdLevel: CrowdLevel,
    modifier: Modifier = Modifier
) {
    val filledBars = when (crowdLevel) {
        CrowdLevel.QUIET -> 1
        CrowdLevel.LIGHT -> 2
        CrowdLevel.BUSY -> 3
        CrowdLevel.PACKED -> 4
        CrowdLevel.UNKNOWN -> 0
    }

    val activeColor = when (crowdLevel) {
        CrowdLevel.QUIET -> Color(0xFF60A5FA)
        CrowdLevel.LIGHT -> Color(0xFF34D399)
        CrowdLevel.BUSY -> Color(0xFFF59E0B)
        CrowdLevel.PACKED -> Color(0xFFEF4444)
        CrowdLevel.UNKNOWN -> VenuColors.BorderDark
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        repeat(4) { index ->
            val barHeight = when (index) {
                0 -> 10.dp
                1 -> 14.dp
                2 -> 18.dp
                else -> 22.dp
            }

            Box(
                modifier = Modifier
                    .width(10.dp)
                    .height(barHeight)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (index < filledBars) activeColor else VenuColors.Border
                    )
            )
        }
    }
}

private fun crowdLevelLabel(crowdLevel: CrowdLevel): String {
    return when (crowdLevel) {
        CrowdLevel.QUIET -> "Quiet"
        CrowdLevel.LIGHT -> "Light"
        CrowdLevel.BUSY -> "Busy"
        CrowdLevel.PACKED -> "Packed"
        CrowdLevel.UNKNOWN -> "Unknown"
    }
}