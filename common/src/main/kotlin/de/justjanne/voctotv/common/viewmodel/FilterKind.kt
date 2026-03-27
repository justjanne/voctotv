package de.justjanne.voctotv.common.viewmodel

sealed interface FilterKind {
    data object Everything

    data object Live
}
