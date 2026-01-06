package de.justjanne.voctotv.ui.player

import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import de.justjanne.voctotv.R
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.util.formatTime
import de.justjanne.voctotv.viewmodel.PlayerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    viewModel: PlayerViewModel,
    lecture: LectureModel?,
    contentPadding: PaddingValues,
) {
    val layoutDirection = LocalLayoutDirection.current

    val progressState = rememberProgressStateWithTickInterval(viewModel.mediaSession.player)
    val playPauseState = rememberPlayPauseButtonState(viewModel.mediaSession.player)

    val uiVisible = remember { mutableStateOf(false) }
    val isSeeking = remember { mutableStateOf(false) }

    val mainInteractionSource = remember { MutableInteractionSource() }

    BackHandler(uiVisible.value) {
        uiVisible.value = false
    }

    val hideJob = remember { mutableStateOf<Job?>(null) }
    val hideScope = rememberCoroutineScope()
    val showUi = remember {
        {
            hideJob.value?.cancel()
            uiVisible.value = true
        }
    }

    LaunchedEffect(uiVisible.value) {
        if (!uiVisible.value) {
            isSeeking.value = false
        }
    }

    val seekPositionMs = remember { mutableStateOf<Long?>(null) }

    val context = LocalActivity.current
    DisposableEffect(context) {
        context?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        context?.window?.apply {
            WindowCompat.getInsetsController(this, decorView)
                .hide(WindowInsetsCompat.Type.systemBars())
        }

        onDispose {
            context?.apply {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
            context?.window?.apply {
                WindowCompat.getInsetsController(this, decorView)
                    .show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    DisposableEffect(viewModel.mediaSession.player, hideJob, uiVisible) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    context?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    context?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        }

        viewModel.mediaSession.player.addListener(listener)
        onDispose {
            viewModel.mediaSession.player.removeListener(listener)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(mainInteractionSource, indication = null) {
                if (uiVisible.value) {
                    context?.window?.apply {
                        WindowCompat.getInsetsController(this, decorView)
                            .hide(WindowInsetsCompat.Type.systemBars())
                    }
                }
                uiVisible.value = !uiVisible.value
            }
    ) {
        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            lecture
                ?.let {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(red = 28, green = 27, blue = 31, alpha = 204),
                                        Color(red = 28, green = 27, blue = 31, alpha = 0),
                                    )
                                )
                            )
                            .padding(32.dp)
                            .padding(
                                start = contentPadding.calculateStartPadding(layoutDirection),
                                end = contentPadding.calculateEndPadding(layoutDirection),
                                top = contentPadding.calculateTopPadding()
                            )
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
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
                            )
                            Text(
                                text = lecture.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        offset = Offset(x = 2f, y = 4f),
                                        blurRadius = 2f
                                    )
                                ),
                                maxLines = 2,
                            )
                        }

                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        ) {
                            CaptionSelection(viewModel.mediaSession.player, playPauseState)
                            AudioSelection(viewModel.mediaSession.player, playPauseState, lecture)
                        }
                    }
                }
        }

        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(red = 28, green = 27, blue = 31, alpha = 0),
                                Color(red = 28, green = 27, blue = 31, alpha = 204)
                            )
                        )
                    )
                    .padding(
                        top = 32.dp,
                        start = contentPadding.calculateStartPadding(layoutDirection),
                        end = contentPadding.calculateEndPadding(layoutDirection),
                        bottom = contentPadding.calculateBottomPadding(),
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Previewbar(
                        viewModel,
                        viewModel.mediaSession.player,
                        seekPositionMs,
                    )
                    Row(
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 16.dp)
                            .graphicsLayer { alpha = if (seekPositionMs.value == null) 1f else 0f }
                    ) {
                        Text(
                            text = formatTime(progressState.currentPositionMs),
                            style = MaterialTheme.typography.labelLarge.copy(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(x = 2f, y = 4f),
                                    blurRadius = 2f
                                )
                            ),
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = formatTime(progressState.durationMs),
                            style = MaterialTheme.typography.labelLarge.copy(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(x = 2f, y = 4f),
                                    blurRadius = 2f
                                )
                            ),
                        )
                    }
                }
                Seekbar(viewModel.mediaSession.player, seekPositionMs)
            }
        }

        AnimatedVisibility(
            uiVisible.value && seekPositionMs.value == null,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            FilledIconButton(
                onClick = {
                    if (viewModel.mediaSession.player.isPlaying) {
                        viewModel.mediaSession.player.pause()
                    } else viewModel.mediaSession.player.play()
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Black,
                ),
                modifier = Modifier.requiredSize(96.dp).shadow(elevation = 8.dp, shape = CircleShape),
            ) {
                Icon(
                    painter = painterResource(if (playPauseState.showPlay) R.drawable.ic_play_arrow else R.drawable.ic_pause),
                    contentDescription = if (playPauseState.showPlay) "Play" else "Pause",
                    tint = LocalContentColor.current,
                    modifier = Modifier.size(32.dp),
                )
            }
        }
    }
}
