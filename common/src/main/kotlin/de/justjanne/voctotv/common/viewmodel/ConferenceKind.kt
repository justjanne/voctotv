package de.justjanne.voctotv.common.viewmodel

import de.justjanne.voctotv.mediacccde.model.ConferenceModel

enum class ConferenceKind {
    CONGRESS,
    GPN,
    CONFERENCE,
    DOCUMENTATIONS,
    ERFA,
    OTHER
}

fun ConferenceModel.kind() = when {
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
