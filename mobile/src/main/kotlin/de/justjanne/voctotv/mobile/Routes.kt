package de.justjanne.voctotv.mobile

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object Routes {
    @Serializable
    data object Home : NavKey

    @Serializable
    data object ConferenceList : NavKey

    @Serializable
    data class Conference(
        val id: String,
    ) : NavKey

    @Serializable
    data class Lecture(
        val id: String,
    ) : NavKey

    @Serializable
    data class Player(
        val id: String,
    ) : NavKey
}
