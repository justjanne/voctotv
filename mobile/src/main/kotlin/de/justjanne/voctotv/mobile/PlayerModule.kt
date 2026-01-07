package de.justjanne.voctotv.mobile

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.cast.CastPlayer
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
    ): Player {
        val exoPlayer =
            ExoPlayer
                .Builder(context)
                .setScrubbingModeParameters(
                    ScrubbingModeParameters.DEFAULT
                        .buildUpon()
                        .setDisabledTrackTypes(emptySet())
                        .build(),
                ).build()
        val castPlayer =
            CastPlayer
                .Builder(context)
                .setLocalPlayer(exoPlayer)
                .build()
        return castPlayer
    }

    @Provides
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: Player,
    ): MediaSession = MediaSession.Builder(context, player).build()
}
