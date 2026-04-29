package com.example.venu.core.core_common.core_ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.example.venu.core.core_common.core_ui.theme.VenuColors

@Composable
fun ReviewsCountHeader(reviewCount: Int) {
    Text(
        text = "Reviews ($reviewCount)",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = VenuColors.TextPrimary
    )
}