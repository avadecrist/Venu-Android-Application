package com.example.venu.core.core_common.core_ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors

@Composable
fun LeaveReviewCard(
    onSubmitReview: (Int, String) -> Unit
) {
    var selectedRating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Leave a review",
                style = MaterialTheme.typography.titleLarge,
                color = VenuColors.TextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(5) { index ->
                    val filled = index < selectedRating
                    TextButton(
                        onClick = { selectedRating = index + 1 },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (filled) "★" else "☆",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (filled) VenuColors.Star else VenuColors.BorderDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text("Share your experience...")
                    },
                    shape = RoundedCornerShape(18.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = VenuColors.Border,
                        unfocusedIndicatorColor = VenuColors.Border,
                        focusedTextColor = VenuColors.TextPrimary,
                        unfocusedTextColor = VenuColors.TextPrimary,
                        focusedPlaceholderColor = VenuColors.TextMuted,
                        unfocusedPlaceholderColor = VenuColors.TextMuted
                    ),
                    singleLine = false,
                    maxLines = 3
                )

                Surface(
                    onClick = {
                        if (selectedRating > 0 && reviewText.isNotBlank()) {
                            onSubmitReview(selectedRating, reviewText.trim())
                            selectedRating = 0
                            reviewText = ""
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    color = VenuColors.SendButtonBg
                ) {
                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Submit review",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}