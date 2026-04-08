package de.justjanne.voctotv.common.watchlater

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import de.justjanne.voctotv.voctoweb.model.LectureModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchLaterRepository
    @Inject
    constructor(
        @ApplicationContext context: Context,
    ) {
        private val json = Json { ignoreUnknownKeys = true }
        private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        private val _items = MutableStateFlow(load())
        val items: StateFlow<List<WatchLaterItem>> = _items.asStateFlow()

        fun contains(guid: String): Boolean = _items.value.any { it.guid == guid }

        fun toggle(lecture: LectureModel) {
            val item = WatchLaterItem.fromLecture(lecture)
            val next =
                if (contains(lecture.guid)) {
                    _items.value.filterNot { it.guid == lecture.guid }
                } else {
                    listOf(item) + _items.value.filterNot { it.guid == lecture.guid }
                }
            save(next)
        }

        fun remove(guid: String) {
            save(_items.value.filterNot { it.guid == guid })
        }

        private fun save(items: List<WatchLaterItem>) {
            _items.value = items
            preferences.edit().putString(KEY_ITEMS, json.encodeToString(items)).apply()
        }

        private fun load(): List<WatchLaterItem> {
            val encoded = preferences.getString(KEY_ITEMS, null) ?: return emptyList()
            return runCatching {
                json.decodeFromString<List<WatchLaterItem>>(encoded)
            }.getOrNull() ?: emptyList()
        }

        companion object {
            private const val PREFS_NAME = "watch_later"
            private const val KEY_ITEMS = "items"
        }
    }
