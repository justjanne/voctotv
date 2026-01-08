/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.viewmodel

import de.justjanne.voctotv.voctoweb.model.ConferenceModel

enum class ConferenceKind {
    CONGRESS,
    GPN,
    CONFERENCE,
    DOCUMENTATIONS,
    ERFA,
    OTHER,
}

fun ConferenceModel.kind() =
    when {
        slug.startsWith("congress/")
        -> ConferenceKind.CONGRESS

        slug.startsWith("conferences/gpn/")
        -> ConferenceKind.GPN

        slug.startsWith("conferences/") || slug.startsWith("events/")
        -> ConferenceKind.CONFERENCE

        slug.startsWith("erfas/")
        -> ConferenceKind.ERFA

        slug.startsWith("documentations/")
        -> ConferenceKind.DOCUMENTATIONS

        else -> ConferenceKind.OTHER
    }
