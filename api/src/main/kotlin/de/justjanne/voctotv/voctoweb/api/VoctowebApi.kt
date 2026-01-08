/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.api

import retrofit2.Retrofit
import retrofit2.create

interface VoctowebApi {
    val conference: ConferenceApi
    val lecture: LectureApi
    val resource: ResourceApi

    companion object {
        fun build(retrofit: Retrofit): VoctowebApi =
            VoctowebApiImpl(
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
