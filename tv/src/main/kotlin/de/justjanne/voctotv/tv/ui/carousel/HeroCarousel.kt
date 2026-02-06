package de.justjanne.voctotv.tv.ui.carousel

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HeroCarousel(
    count: Int,
    modifier: Modifier = Modifier,
    pageContent: @Composable (page: Int) -> Unit,
) {
    val isCarouselFocused = remember { mutableStateOf(false) }

    if (count > 0) {
        Carousel(
            itemCount = count,
            modifier =
                modifier
                    .height(HeroCarouselDefaults.ContainerHeight)
                    .fillMaxWidth()
                    .padding(HeroCarouselDefaults.ContainerPadding)
                    .border(
                        HeroCarouselDefaults.border(isCarouselFocused.value),
                        HeroCarouselDefaults.Shape
                    ).clip(HeroCarouselDefaults.Shape)
                    .onFocusChanged {
                        // Because the carousel itself never gets the focus
                        isCarouselFocused.value = it.hasFocus
                    },
            contentTransformStartToEnd =
                fadeIn(tween(durationMillis = 1000))
                    .togetherWith(fadeOut(tween(durationMillis = 1000))),
            contentTransformEndToStart =
                fadeIn(tween(durationMillis = 1000))
                    .togetherWith(fadeOut(tween(durationMillis = 1000))),
        ) { index ->
            pageContent(index)
        }
    }
}
