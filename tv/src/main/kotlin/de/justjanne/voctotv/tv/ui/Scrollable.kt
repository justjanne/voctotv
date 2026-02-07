package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.util.fastRoundToInt
import androidx.tv.material3.LocalTextStyle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun Modifier.scrollable(scrollState: ScrollState): Modifier {
    val lineHeight =
        with(LocalDensity.current) {
            LocalTextStyle.current.lineHeight.toPx()
        }
    val scrollAmount = lineHeight.times(5).fastRoundToInt()

    val scope = rememberCoroutineScope()

    val scrollJob = remember { mutableStateOf<Job?>(null) }

    fun scrollBy(offset: Int) {
        scrollJob.value?.cancel()
        scrollJob.value =
            scope.launch {
                scrollState.animateScrollTo(scrollState.value + offset)
                scrollJob.value = null
            }
    }

    return this then
        Modifier
            .verticalScroll(scrollState)
            .focusable(true)
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyUp) {
                    false
                } else {
                    when (event.key) {
                        Key.DirectionUp -> {
                            scrollBy(-scrollAmount)
                            true
                        }

                        Key.DirectionDown -> {
                            scrollBy(scrollAmount)
                            true
                        }

                        else -> false
                    }
                }
            }
}
