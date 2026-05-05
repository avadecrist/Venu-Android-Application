package com.example.venu.core.core_common.eventdetails

//import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material.icons.outlined.Verified
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.venu.core.core_common.core_ui.components.LeaveReviewCard
import com.example.venu.core.core_common.core_ui.components.ReviewCard
import com.example.venu.core.core_common.core_ui.components.ReviewsCountHeader
import com.example.venu.core.core_common.core_ui.theme.VenuColors
import com.example.venu.core.core_common.util.formatDistance
import com.example.venu.core.core_common.util.formatOneDecimal
import com.example.venu.core.core_domain.model.CrowdLevel
import com.example.venu.core.core_presentation.EventDetailsUi
import com.example.venu.core.core_presentation.ReviewUi
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage


@Composable
fun EventDetailsSheet(
    event: EventDetailsUi,
    onBack: () -> Unit,
    onSaveClick: () -> Unit,
    onGetDirectionsClick: () -> Unit,
    onSubmitReview: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 8.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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
                subtitle = event.subtitle
            )
        }

        item {
            EventHeroImage(
                imageUrl = event.imageUrl,
                isVerifiedVenue = event.isVerifiedVenue
            )
        }

        item {
            ActionButtonsRow(
                onGetDirectionsClick = onGetDirectionsClick
            )

            Spacer(modifier = Modifier.height(22.dp))

            RatingSummarySection(
                credibilityScore = event.credibilityScore,
                averageRating = event.averageRating ?: 0.0,//event.averageRating,
                googleRating = event.googleRating ?: 0.0,
                userRating = event.userRating ?: 0.0,
                reviewCount = event.reviewCount
            )

            Spacer(modifier = Modifier.height(6.dp))
        }

        item {
            InfoGridSection(
                genre = event.genre,
                locationName = event.locationName,
                startTimeLabel = event.startTimeLabel,
                attendeeCount = event.attendeeCount,
                crowdLevel = event.crowdLevel
            )
        }

        item {
            PriceAndVerifiedCard(
                priceText = event.priceText,
                isVerified = event.isVerifiedVenue
            )
        }

        item {
            ReviewsCountHeader(reviewCount = event.reviewCount)
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
    subtitle: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = VenuColors.TextPrimary,
            fontWeight = FontWeight.Bold
        )

        if (subtitle.isNotBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = VenuColors.TextSecondary
            )
        }
    }
}

@Composable
private fun EventHeroImage(
    imageUrl: String?,
    isVerifiedVenue: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(VenuColors.SurfaceMuted)
    ) {
        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Event location image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(999.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
            ) {
                Text(
                    text = "Image via Google Maps",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = VenuColors.TextSecondary
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = VenuColors.TextMuted,
                    modifier = Modifier.size(42.dp)
                )

                Text(
                    text = "Event image via Google Maps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = VenuColors.TextMuted
                )
            }
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
            icon = Icons.AutoMirrored.Filled.DirectionsWalk,
            onClick = onGetDirectionsClick,
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        label = "action_button_scale"
    )

    val elevation by animateDpAsState(
        targetValue = 2.dp,
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
        shape = RoundedCornerShape(20.dp),
        color = VenuColors.AccentBlue,
        shadowElevation = elevation,
        border = BorderStroke(1.dp, VenuColors.AccentBlue)
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
                tint = VenuColors.Background
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Get Directions",
                color = VenuColors.Background,
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
    attendeeCount: Int,
    crowdLevel: CrowdLevel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailInfoCard(
                modifier = Modifier.weight(1f).height(130.dp),
                label = "Genre",
                value = genreLabel(genre),
                icon = Icons.Outlined.Sell,
                accentChip = genreChipText(genre)
            )

            DetailInfoCard(
                modifier = Modifier.weight(1f).height(130.dp),
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
                modifier = Modifier.weight(1f).height(130.dp),
                label = "Start Time",
                value = startTimeLabel,
                icon = Icons.Outlined.Schedule
            )

            AttendeesInfoCard(
                modifier = Modifier.weight(1f).height(130.dp),
                attendeeCount = attendeeCount,
                crowdLevel = crowdLevel
            )
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
        color = MaterialTheme.colorScheme.onPrimary,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = priceText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
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


private val PreviewEvent = EventDetailsUi(
    id = "ramen-popup-001",
    name = "Ramen Pop-Up",
    subtitle = "Authentic ramen from a local chef. Limited bowls, first come first served.",
    genre = Genre.FOOD,
    locationName = "East Quad",
    latitude = 0.0,
    longitude = 0.0,
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
    crowdLevel = CrowdLevel.BUSY,
    isSaved = false,
    reviews = listOf(
        ReviewUi(
            id = "r1",
            displayName = "Avery C.",
            authorInitial = "A",
            rating = 5,
            comment = "Best ramen I've had on campus. The broth was perfect.",
            timeAgo = "1d ago"
        ),
        ReviewUi(
            id = "r2",
            displayName = "Jordan M.",
            authorInitial = "J",
            rating = 4,
            comment = "Really good and worth the wait. Noodles were great, line moved a little slow.",
            timeAgo = "2d ago"
        ),
        ReviewUi(
            id = "r3",
            displayName = "Sophia T.",
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
            onBack = {},
            onSaveClick = {},
            onGetDirectionsClick = {},
            onSubmitReview = { _, _ -> }
        )
    }
}

@Preview(name = "Quiet Event", showBackground = true)
@Composable
fun PreviewQuiet() {
    EventDetailsSheet(
        event = PreviewEvent.copy(
            name = "Late Night Study",
            attendeeCount = 12,
            crowdLevel = CrowdLevel.QUIET
        ),
        onBack = {},
        onSaveClick = {},
        onGetDirectionsClick = {},
        onSubmitReview = { _, _ -> }
    )
}

@Preview(name = "Packed Event", showBackground = true)
@Composable
fun PreviewPacked() {
    EventDetailsSheet(
        event = PreviewEvent.copy(
            name = "Campus Festival",
            attendeeCount = 240,
            crowdLevel = CrowdLevel.PACKED
        ),
        onBack = {},
        onSaveClick = {},
        onGetDirectionsClick = {},
        onSubmitReview = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun InteractivePreview() {

    EventDetailsSheet(
        event = PreviewEvent,
        onBack = {},
        onSaveClick = {},
        onGetDirectionsClick = {},
        onSubmitReview = { _, _ -> }
    )
}