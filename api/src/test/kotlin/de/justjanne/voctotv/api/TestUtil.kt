/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.api

import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.InputStream

object TestUtil {
    fun buildApi(): VoctowebApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client: OkHttpClient =
            OkHttpClient
                .Builder()
                .addNetworkInterceptor { chain ->
                    chain.proceed(
                        chain
                            .request()
                            .newBuilder()
                            .header("User-Agent", "VoctowebApi.kt/0.0.1")
                            .build(),
                    )
                }.addInterceptor(interceptor)
                .build()
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

    fun load(filename: String): InputStream =
        requireNotNull(javaClass.classLoader.getResourceAsStream(filename)) {
            "Could not load test resource: $filename"
        }
}
