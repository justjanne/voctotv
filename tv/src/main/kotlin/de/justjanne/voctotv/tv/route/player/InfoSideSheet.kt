package de.justjanne.voctotv.tv.route.player

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastJoinToString
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.ModalSideSheet
import de.justjanne.voctotv.tv.ui.ModalSideSheetDefaults
import de.justjanne.voctotv.tv.ui.TagChip
import de.justjanne.voctotv.tv.ui.scrollable
import de.justjanne.voctotv.tv.ui.scrollbar
import de.justjanne.voctotv.tv.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.tv.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun InfoSideSheet(lecture: LectureModel) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(lecture) {
        focusRequester.requestFocus()
    }

    ModalSideSheet {
        Column(
            Modifier
                .scrollbar(scrollState, 16.dp)
                .scrollable(scrollState)
                .padding(ModalSideSheetDefaults.Padding)
                .padding(end = 16.dp)
                .focusRequester(focusRequester)
                .onPreviewKeyEvent {
                    if (it.type == KeyEventType.KeyDown) {
                        when (it.key) {
                            Key.MediaFastForward, Key.MediaStepForward, Key.MediaSkipForward,
                            Key.MediaRewind, Key.MediaStepBackward, Key.MediaSkipBackward,
                            -> {
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                },
        ) {
            Text(
                lecture.title,
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        lineBreak = LineBreak.Heading,
                    ),
            )

            if (!lecture.subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    lecture.subtitle ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        String.format(Locale.current.platformLocale, "%,d", lecture.viewCount),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Text(
                        stringResource(R.string.video_info_views),
                        fontSize = 12.sp,
                        color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        DateUtils.formatDateTime(
                            LocalContext.current,
                            lecture.releaseDate.toInstant().toEpochMilli(),
                            DateUtils.FORMAT_SHOW_DATE or
                                DateUtils.FORMAT_NO_YEAR or
                                DateUtils.FORMAT_ABBREV_MONTH,
                        ),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    )
                    Text(
                        lecture.releaseDate.year.toString(),
                        fontSize = 12.sp,
                        color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                lecture.persons.fastJoinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                lecture.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
            )

            Spacer(Modifier.height(16.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
                verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top),
            ) {
                for (tag in lecture.tags) {
                    TagChip { Text(tag) }
                }
            }
        }
    }
}
