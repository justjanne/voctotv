package de.ccc.media.api

import retrofit2.Retrofit
import retrofit2.create

interface VoctowebApi {
    val conference: ConferenceApi
    val lecture: LectureApi
    val resource: ResourceApi

    companion object {
        fun build(retrofit: Retrofit): VoctowebApi = VoctowebApiImpl(
            retrofit.create<ConferenceApi>(),
            retrofit.create<LectureApi>(),
            retrofit.create<ResourceApi>(),
        )
    }
}

private data class VoctowebApiImpl(
    override val conference: ConferenceApi,
    override val lecture: LectureApi,
    override val resource: ResourceApi,
) : VoctowebApi
