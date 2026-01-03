package de.justjanne.voctotv

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.justjanne.voctotv.mediacccde.api.VoctowebApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {
    @Provides
    fun provideApi(): VoctowebApi {
        val client: OkHttpClient = OkHttpClient.Builder().addNetworkInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder().header("User-Agent", "VoctowebApi.kt/0.0.1").build()
            )
        }.build()
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder().baseUrl("https://api.media.ccc.de/")
            .addConverterFactory(Json.asConverterFactory(contentType)).client(client).build()
        return VoctowebApi.build(retrofit)
    }
}
