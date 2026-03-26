/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveConferenceModel(
    @SerialName("conference")
    val conference: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("author")
    val author: String,
    @SerialName("description")
    val description: String,
    @SerialName("keywords")
    val keywords: String,
    @SerialName("schedule")
    val schedule: String,
    @SerialName("startsAt")
    val startsAt: Timestamp,
    @SerialName("endsAt")
    val endsAt: Timestamp,
    @SerialName("isCurrentlyStreaming")
    val isCurrentlyStreaming: Boolean,
    @SerialName("groups")
    val groups: List<LiveGroupModel>,
)
