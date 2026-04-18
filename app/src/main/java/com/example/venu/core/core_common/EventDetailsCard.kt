package com.example.venu.core.core_common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme as M3Theme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.venu.core.core_domain.model.Genre
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer


data class ReviewUi(
    val id: String,
    val authorName: String,
    val authorInitial: String,
    val rating: Int,
    val comment: String,
    val timeAgo: String
)

data class EventDetailsUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val genre: Genre,
    val locationName: String,
    val distanceKm: Double?,
    val priceText: String,
    val startTimeLabel: String,
    val imageUrl: String? = null,
    val credibilityScore: Int,
    val reviewCount: Int,
    val isVerifiedVenue: Boolean,
    val averageRating: Double,
    val googleRating: Double,
    val userRating: Double,
    val attendeeCount: Int,
    val reviews: List<ReviewUi>,
    val isSaved: Boolean = false
)

@Composable
fun EventDetailsSheet(
    event: EventDetailsUi,
    showDirectionsButton: Boolean,
    onBack: () -> Unit,
    onSaveClick: () -> Unit,
    onViewOnMapClick: () -> Unit,
    onGetDirectionsClick: () -> Unit,
    onSubmitReview: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(VenuColors.Background),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 8.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SheetHandle()
        }

        item {
            TopBar(
                isSaved = event.isSaved,
                onBack = onBack,
                onSaveClick = onSaveClick
            )
        }

        item {
            TitleSection(
                title = event.name,
                subtitle = event.subtitle,
                locationName = event.locationName,
                distanceKm = event.distanceKm,
                priceText = event.priceText
            )
        }

        item {
            HeroImagePlaceholder(
                isVerifiedVenue = event.isVerifiedVenue
            )
        }

        item {
            ActionButtonsRow(
                showDirectionsButton = showDirectionsButton,
                onViewOnMapClick = onViewOnMapClick,
                onGetDirectionsClick = onGetDirectionsClick
            )
        }

        item {
            RatingSummarySection(
                credibilityScore = event.credibilityScore,
                averageRating = event.averageRating,
                googleRating = event.googleRating,
                userRating = event.userRating,
                reviewCount = event.reviewCount
            )
        }

        item {
            InfoGridSection(
                genre = event.genre,
                locationName = event.locationName,
                startTimeLabel = event.startTimeLabel,
                attendeeCount = event.attendeeCount
            )
        }

        item {
            PriceAndVerifiedCard(
                priceText = event.priceText,
                isVerified = event.isVerifiedVenue
            )
        }

        item {
            ReviewsHeader(reviewCount = event.reviewCount)
        }

        item {
            LeaveReviewCard(onSubmitReview = onSubmitReview)
        }

        items(event.reviews, key = { it.id }) { review ->
            ReviewCard(review = review)
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun SheetHandle() {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(100))
                .background(VenuColors.Handle)
        )
    }
}

@Composable
private fun TopBar(
    isSaved: Boolean,
    onBack: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleActionButton(
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = VenuColors.TextPrimary
            )
        }

        CircleActionButton(
            onClick = onSaveClick
        ) {
            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = if (isSaved) "Saved" else "Save",
                tint = if (isSaved) VenuColors.AccentBlue else VenuColors.TextSecondary
            )
        }
    }
}

@Composable
private fun CircleActionButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun TitleSection(
    title: String,
    subtitle: String,
    locationName: String,
    distanceKm: Double?,
    priceText: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = VenuColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = VenuColors.TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        val meta = buildString {
            append(locationName)
            distanceKm?.let {
                append(" • ")
                append(formatDistance(it))
            }
            append(" • ")
            append(priceText)
        }

        Text(
            text = meta,
            style = MaterialTheme.typography.bodyMedium,
            color = VenuColors.TextMuted
        )
    }
}

@Composable
private fun HeroImagePlaceholder(
    isVerifiedVenue: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(VenuColors.SurfaceMuted)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = VenuColors.TextMuted,
                modifier = Modifier.size(42.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Event image via Google Maps",
                color = VenuColors.TextMuted,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isVerifiedVenue) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                shape = RoundedCornerShape(999.dp),
                color = VenuColors.VerifiedBg
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Verified,
                        contentDescription = null,
                        tint = VenuColors.VerifiedText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Verified",
                        style = MaterialTheme.typography.bodyMedium,
                        color = VenuColors.VerifiedText,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButtonsRow(
    showDirectionsButton: Boolean,
    onViewOnMapClick: () -> Unit,
    onGetDirectionsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionButton(
            modifier = Modifier.weight(1f),
            text = "View on Map",
            icon = Icons.Outlined.LocationOn,
            onClick = onViewOnMapClick,
            isActive = false
        )

        AnimatedVisibility(
            visible = showDirectionsButton,
            modifier = Modifier.weight(1f),
            enter = fadeIn() + slideInHorizontally { it / 3 },
            exit = fadeOut() + slideOutHorizontally { it / 3 }
        ) {
            ActionButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Get Directions",
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                onClick = onGetDirectionsClick,
                isActive = true
            )
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isActive) VenuColors.AccentBlue else Color.White,
        label = "action_button_bg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isActive) Color.White else VenuColors.AccentBlue,
        label = "action_button_content"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isActive) VenuColors.AccentBlue else VenuColors.AccentBlueBorder,
        label = "action_button_border"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        label = "action_button_scale"
    )

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else if (isActive) 8.dp else 0.dp,
        label = "action_button_elevation"
    )

    Surface(
        modifier = modifier
            .height(56.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        onClick = onClick,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(if (isActive) 20.dp else 18.dp),
        color = backgroundColor,
        shadowElevation = elevation,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun RatingSummarySection(
    credibilityScore: Int,
    averageRating: Double,
    googleRating: Double,
    userRating: Double,
    reviewCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val scoreBg = when {
            credibilityScore >= 80 -> VenuColors.ScoreHigh
            credibilityScore >= 60 -> VenuColors.ScoreMedium
            else -> VenuColors.ScoreLow
        }

        Box(
            modifier = Modifier
                .size(78.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(scoreBg),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = credibilityScore.toString(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "Overall Rating",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = VenuColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "VENU: ${formatOneDecimal(userRating)}   •   Google: ${formatOneDecimal(googleRating)}",
                style = MaterialTheme.typography.bodyLarge,
                color = VenuColors.TextSecondary
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "$reviewCount reviews • Avg ${formatOneDecimal(averageRating)}",
                style = MaterialTheme.typography.bodyMedium,
                color = VenuColors.TextMuted
            )
        }
    }
}

@Composable
private fun InfoGridSection(
    genre: Genre,
    locationName: String,
    startTimeLabel: String,
    attendeeCount: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailInfoCard(
                modifier = Modifier.weight(1f),
                label = "Genre",
                value = genreLabel(genre),
                icon = Icons.Outlined.Sell,
                accentChip = genreChipText(genre)
            )

            DetailInfoCard(
                modifier = Modifier.weight(1f),
                label = "Location",
                value = locationName,
                icon = Icons.Outlined.LocationOn
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailInfoCard(
                modifier = Modifier.weight(1f),
                label = "Start Time",
                value = startTimeLabel,
                icon = Icons.Outlined.Schedule
            )

            DetailInfoCard(
                modifier = Modifier.weight(1f),
                label = "Attendees",
                value = "$attendeeCount going",
                icon = Icons.Outlined.Group
            )
        }
    }
}

@Composable
private fun DetailInfoCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    accentChip: String? = null
) {
    Surface(
        modifier = modifier,
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

@Composable
private fun PriceAndVerifiedCard(
    priceText: String,
    isVerified: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Price",
                    style = MaterialTheme.typography.titleMedium,
                    color = VenuColors.TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = priceText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = VenuColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            if (isVerified) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = VenuColors.VerifiedBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Verified,
                            contentDescription = null,
                            tint = VenuColors.VerifiedText,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Verified Event",
                            style = MaterialTheme.typography.titleMedium,
                            color = VenuColors.VerifiedText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReviewsHeader(reviewCount: Int) {
    Text(
        text = "Reviews ($reviewCount)",
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = VenuColors.TextPrimary
    )
}

@Composable
private fun LeaveReviewCard(
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

@Composable
private fun ReviewCard(
    review: ReviewUi
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        border = BorderStroke(1.dp, VenuColors.Border)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                ReviewerAvatar(initial = review.authorInitial)

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = review.authorName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = VenuColors.TextPrimary
                    )
                }

                Text(
                    text = review.timeAgo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VenuColors.TextMuted
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = buildStarString(review.rating),
                style = MaterialTheme.typography.headlineSmall,
                color = VenuColors.Star
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyLarge,
                color = VenuColors.TextSecondary
            )
        }
    }
}

@Composable
private fun ReviewerAvatar(initial: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(VenuColors.AvatarBg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            color = VenuColors.AccentBlue,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatDistance(distanceKm: Double): String {
    return if (distanceKm < 1.0) {
        "${(distanceKm * 1000).toInt()} m away"
    } else {
        "${String.format("%.1f", distanceKm)} km away"
    }
}

private fun formatOneDecimal(value: Double): String {
    return String.format("%.1f", value)
}

private fun buildStarString(rating: Int): String {
    return "★".repeat(rating.coerceIn(0, 5)) + "☆".repeat((5 - rating).coerceIn(0, 5))
}

private fun genreLabel(genre: Genre): String {
    return when (genre) {
        Genre.FOOD -> "Food"
        Genre.STUDY -> "Study"
        Genre.MUSIC -> "Music"
        Genre.SPORTS -> "Sports"
        Genre.MUSEUMS -> "Museums"
        Genre.COFFEE -> "Coffee"
        Genre.NIGHTLIFE -> "Nightlife"
        Genre.OUTDOORS -> "Outdoors"
    }
}

private fun genreChipText(genre: Genre): String {
    return when (genre) {
        Genre.FOOD -> "🍔 Food"
        Genre.STUDY -> "📚 Study"
        Genre.MUSIC -> "🎵 Music"
        Genre.SPORTS -> "🏀 Sports"
        Genre.MUSEUMS -> "🖼️ Museums"
        Genre.COFFEE -> "☕ Coffee"
        Genre.NIGHTLIFE -> "🌙 Nightlife"
        Genre.OUTDOORS -> "🌿 Outdoors"
    }
}

private object VenuColors {
    val Background = Color(0xFFFFFFFF)
    val SurfaceMuted = Color(0xFFF5F5F7)
    val Border = Color(0xFFE7E7EC)
    val BorderDark = Color(0xFFCFCFD7)
    val Handle = Color(0xFFE6E6EA)

    val TextPrimary = Color(0xFF1F2430)
    val TextSecondary = Color(0xFF6B7280)
    val TextMuted = Color(0xFF9CA3AF)

    val AccentBlue = Color(0xFF3B82F6)
    val AccentBlueBorder = Color(0xFFCFE0FF)
    val SendButtonBg = Color(0xFFAFC7F7)

    val GenreChipBg = Color(0xFFFFE9DB)
    val GenreChipText = Color(0xFFF97316)

    val VerifiedBg = Color(0xFFE8F7EE)
    val VerifiedText = Color(0xFF27AE60)

    val ScoreHigh = Color(0xFF22C55E)
    val ScoreMedium = Color(0xFFF59E0B)
    val ScoreLow = Color(0xFFEF4444)

    val Star = Color(0xFFF5A623)
    val AvatarBg = Color(0xFFEAEFFD)
}

private val PreviewEvent = EventDetailsUi(
    id = "ramen-popup-001",
    name = "Ramen Pop-Up",
    subtitle = "Authentic ramen from a local chef. Limited bowls, first come first served.",
    genre = Genre.FOOD,
    locationName = "East Quad",
    distanceKm = 0.4,
    priceText = "$8",
    startTimeLabel = "Feb 19 • 12:00 PM",
    imageUrl = null,
    credibilityScore = 95,
    reviewCount = 3,
    isVerifiedVenue = true,
    averageRating = 4.9,
    googleRating = 5.0,
    userRating = 4.9,
    attendeeCount = 95,
    isSaved = false,
    reviews = listOf(
        ReviewUi(
            id = "r1",
            authorName = "Avery C.",
            authorInitial = "A",
            rating = 5,
            comment = "Best ramen I've had on campus. The broth was perfect.",
            timeAgo = "1d ago"
        ),
        ReviewUi(
            id = "r2",
            authorName = "Jordan M.",
            authorInitial = "J",
            rating = 4,
            comment = "Really good and worth the wait. Noodles were great, line moved a little slow.",
            timeAgo = "2d ago"
        ),
        ReviewUi(
            id = "r3",
            authorName = "Sophia T.",
            authorInitial = "S",
            rating = 5,
            comment = "Would absolutely go again. Super fun atmosphere and quality food.",
            timeAgo = "3d ago"
        )
    )
)

@Preview(
    name = "Event Details Sheet",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    widthDp = 400,
    heightDp = 1400
)
@Composable
private fun EventDetailsSheetPreview() {
    MaterialTheme {
        EventDetailsSheet(
            event = PreviewEvent,
            showDirectionsButton = true,
            onBack = {},
            onSaveClick = {},
            onViewOnMapClick = {},
            onGetDirectionsClick = {},
            onSubmitReview = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InteractivePreview() {
    var showDirections by remember { mutableStateOf(false) }

    EventDetailsSheet(
        event = PreviewEvent,
        showDirectionsButton = showDirections,
        onBack = {},
        onSaveClick = {},
        onViewOnMapClick = {
            showDirections = true // simulate navigation result
        },
        onGetDirectionsClick = {},
        onSubmitReview = { _, _ -> }
    )
}