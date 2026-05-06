package com.example.venu.core.core_common.core_ui.components

import android.util.Log
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_common.core_ui.theme.VenuTheme
import com.example.venu.features.reviews.model.ReviewDraft

@Composable
fun LeaveReviewCard(
    draft: ReviewDraft?,
    isSubmitting: Boolean,
    onRatingChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val rating = draft?.rating ?: 0
    val comment = draft?.comment.orEmpty()

    var isTextFieldFocused by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Leave a review",
                style = MaterialTheme.typography.titleLarge,
                color = if (isTextFieldFocused) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                },
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(5) { index ->
                    val filled = index < rating

                    TextButton(
                        onClick = { onRatingChange(index + 1) },
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
                    value = comment,
                    onValueChange = onCommentChange,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged {
                            isTextFieldFocused = it.isFocused
                        },
                    placeholder = { Text("Share your experience...") },
                    shape = RoundedCornerShape(18.dp),
                    enabled = !isSubmitting,
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Surface(
                    onClick = {

                        if (!isSubmitting && rating > 0 && comment.isNotBlank()) {
                            onSubmit()
                        }
                    },
                    shape = RoundedCornerShape(18.dp),
                    color = if (isTextFieldFocused) {
                        VenuColors.AccentBlue
                    } else {
                        VenuColors.SendButtonBg
                    },
                ) {
                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Submit review",
                            tint = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Leave Review Card - Light",
    showBackground = true
)
@Composable
private fun LeaveReviewCardLightPreview() {
    VenuTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LeaveReviewCard(
                draft = ReviewDraft(
                    rating = 4,
                    eventId = "preview",
                    comment = "Great spot, fun crowd, and good energy."
                ),
                isSubmitting = false,
                onRatingChange = {},
                onCommentChange = {},
                onSubmit = {}
            )
        }
    }
}

@Preview(
    name = "Leave Review Card - Dark",
    showBackground = true
)
@Composable
private fun LeaveReviewCardDarkPreview() {
    VenuTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            LeaveReviewCard(
                draft = ReviewDraft(
                    rating = 4,
                    eventId = "preview",
                    comment = "Great spot, fun crowd, and good energy."
                ),
                isSubmitting = false,
                onRatingChange = {},
                onCommentChange = {},
                onSubmit = {}
            )
        }
    }
}