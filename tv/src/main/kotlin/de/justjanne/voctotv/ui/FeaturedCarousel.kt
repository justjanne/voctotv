package de.justjanne.voctotv.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import coil3.compose.AsyncImage
import de.justjanne.voctotv.mediacccde.model.LectureModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(
    lectures: List<LectureModel>,
    openLecture: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isCarouselFocused = remember { mutableStateOf(false) }
    val alpha = if (isCarouselFocused.value) {
        1f
    } else {
        0f
    }

    if (lectures.isNotEmpty()) {
        Carousel(
            itemCount = lectures.size,
            modifier = modifier.height(361.dp)
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, bottom = 20.dp).border(
                    width = 3.dp,
                    color = Color.White.copy(alpha = alpha),
                    shape = ShapeDefaults.Medium,
                ).clip(ShapeDefaults.Medium)
                .onFocusChanged {
                    // Because the carousel itself never gets the focus
                    isCarouselFocused.value = it.hasFocus
                },
            contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000))
                .togetherWith(fadeOut(tween(durationMillis = 1000))),
            contentTransformEndToStart = fadeIn(tween(durationMillis = 1000))
                .togetherWith(fadeOut(tween(durationMillis = 1000))),
        ) { index ->
            val lecture = lectures[index]

            Card(
                onClick = { openLecture(lecture.guid) }, colors = CardDefaults.colors(
                    containerColor = Color.Black,
                    focusedContainerColor = Color.Black,
                    pressedContainerColor = Color.Black,
                )
            ) {
                Box {
                    CarouselItemBackground(
                        lecture,
                        modifier = Modifier.fillMaxSize().padding(start = 288.dp),
                    )
                    CarouselItemForeground(
                        lecture,
                        modifier = Modifier.fillMaxSize(),
                        openLecture = openLecture,
                    )
                }
            }
        }
    }
}

@Composable
private fun CarouselItemForeground(
    lecture: LectureModel,
    modifier: Modifier = Modifier,
    openLecture: (String) -> Unit,
) {
    Box(
        modifier = modifier, contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().width(418.dp).padding(32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = lecture.conferenceTitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.65f),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = lecture.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 2,
            )
            lecture.description?.let {
                Text(
                    text = it, style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.65f),
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            offset = Offset(x = 2f, y = 4f),
                            blurRadius = 2f
                        )
                    ),
                    maxLines = 3,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            WatchNowButton(onClick = {
                openLecture(lecture.guid)
            })
        }
    }
}

@Composable
private fun CarouselItemBackground(lecture: LectureModel, modifier: Modifier = Modifier) {
    AsyncImage(
        model = lecture.posterUrl,
        contentDescription = lecture.title,
        modifier = modifier.drawWithContent {
            drawContent()
            drawRect(
                Brush.radialGradient(
                    center = Offset(Float.POSITIVE_INFINITY, 0f),
                    colors = listOf(
                        Color.Transparent,
                        Color.Black,
                    ),
                    radius = size.maxDimension,
                ),
                size = size,
            )
        },
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun WatchNowButton(
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 20.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        colors = ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface,
            focusedContentColor = MaterialTheme.colorScheme.surface,
        ),
        scale = ButtonDefaults.scale(scale = 1f)
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "Watch now",
            style = MaterialTheme.typography.titleSmall
        )
    }
}
