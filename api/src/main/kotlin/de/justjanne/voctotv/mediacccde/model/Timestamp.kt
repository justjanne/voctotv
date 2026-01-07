/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mediacccde.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

typealias Timestamp =
    @Serializable(with = TimestampSerializer::class)
    OffsetDateTime

object TimestampSerializer : KSerializer<OffsetDateTime> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor

    override fun serialize(
        encoder: Encoder,
        value: OffsetDateTime,
    ) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

    override fun deserialize(decoder: Decoder): OffsetDateTime =
        OffsetDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
