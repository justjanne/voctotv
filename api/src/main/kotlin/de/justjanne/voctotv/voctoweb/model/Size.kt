/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias Size =
    @Serializable(with = SizeSerializer::class)
    Pair<Int, Int>

object SizeSerializer : KSerializer<Pair<Int, Int>> {
    private val delegate = ListSerializer(Int.serializer())

    override val descriptor: SerialDescriptor = delegate.descriptor

    override fun serialize(
        encoder: Encoder,
        value: Pair<Int, Int>,
    ) {
        delegate.serialize(encoder, listOf(value.first, value.second))
    }

    override fun deserialize(decoder: Decoder): Pair<Int, Int> {
        val value = delegate.deserialize(decoder)
        return Pair(value[0], value[1])
    }
}
