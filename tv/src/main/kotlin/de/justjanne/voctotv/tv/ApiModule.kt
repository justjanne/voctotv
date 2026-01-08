/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.justjanne.voctotv.common.util.UserAgentInterceptor
import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {
    @Provides
    fun provideApi(client: OkHttpClient): VoctowebApi {
        val contentType = "application/json".toMediaType()
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl("https://api.media.ccc.de/")
                .addConverterFactory(Json.asConverterFactory(contentType))
                .client(client)
                .build()
        return VoctowebApi.build(retrofit)
    }

    @Provides
    fun provideClient(
        @ApplicationContext context: Context,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addNetworkInterceptor(
                UserAgentInterceptor("${BuildConfig.APPLICATION_ID}/${BuildConfig.VERSION_NAME}"),
            ).cache(
                Cache(
                    directory = File(context.cacheDir, "http_cache"),
                    maxSize = 50L * 1024L * 1024L,
                ),
            ).build()
}
