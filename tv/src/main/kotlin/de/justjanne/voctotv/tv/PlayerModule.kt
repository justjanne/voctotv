/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ScrubbingModeParameters
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
internal object PlayerModule {
    @OptIn(UnstableApi::class)
    @Provides
    fun providePlayer(
        @ApplicationContext context: Context,
    ): Player =
        ExoPlayer
            .Builder(context)
            .setScrubbingModeParameters(
                ScrubbingModeParameters.DEFAULT
                    .buildUpon()
                    .setDisabledTrackTypes(emptySet())
                    .build(),
            ).build()

    @Provides
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: Player,
    ): MediaSession = MediaSession.Builder(context, player).build()
}
