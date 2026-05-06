package com.example.venu.core.core_common.core_ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors

@Composable
fun BaseEventCard(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit,
    width: Dp? = null,
    height: Dp? = null,
    contentPadding: Dp = 16.dp,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(24.dp)

    val border = BorderStroke(
        width = if (selected) 3.dp else 2.dp,
        color = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            borderColor
        }
    )

    Card(
        modifier = modifier
            .then(if (width != null) Modifier.width(width) else Modifier)
            .then(if (height != null) Modifier.height(height) else Modifier)
            .clickable { onClick() },
        shape = shape,
        border = border,
        colors = CardDefaults.cardColors(
            containerColor = contentColor//MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}