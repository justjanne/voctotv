package de.justjanne.voctotv.api

import de.justjanne.voctotv.voctoweb.deeplink.DeepLink
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeepLinkTest {
    companion object {
        fun match(url: String): DeepLink? {
            val httpUrl = url.toHttpUrl()
            return DeepLink.match(httpUrl.host, httpUrl.encodedPath)
        }
    }

    @Nested
    inner class StaticRoutesTest {
        @ParameterizedTest
        @ValueSource(
            strings = [
                "https://media.ccc.de/",
                "https://media.ccc.de/#",
            ],
        )
        fun `root maps to Index`(url: String) {
            assertEquals(DeepLink.Index, match(url))
        }

        @ParameterizedTest
        @ValueSource(
            strings = [
                "https://media.ccc.de/about.html",
                "https://media.ccc.de/about.html#apps",
                "https://media.ccc.de/about.html#privacy",
            ],
        )
        fun `about maps to About`(url: String) {
            assertEquals(DeepLink.About, match(url))
        }
    }

    @Nested
    inner class FeedRoutesTest {
        @ParameterizedTest
        @ValueSource(
            strings = [
                "https://media.ccc.de/news.atom",
                "https://media.ccc.de/updates.rdf",
                "https://media.ccc.de/podcast-hq.xml",
                "https://media.ccc.de/podcast-lq.xml",
                "https://media.ccc.de/podcast-audio-only.xml",
                "https://media.ccc.de/podcast-archive-hq.xml",
                "https://media.ccc.de/podcast-archive-lq.xml",
            ],
        )
        fun `feeds are not deep links`(url: String) {
            assertNull(match(url))
        }
    }

    @Nested
    inner class EventShowRoutesTest {
        @ParameterizedTest
        @CsvSource(
            "https://media.ccc.de/v/39c3-security-nightmares, 39c3-security-nightmares",
            "https://media.ccc.de/v/eth0_2025-56501-closing-talk, eth0_2025-56501-closing-talk",
            "https://media.ccc.de/v/denog17-81769-denog17-closing, denog17-81769-denog17-closing",
            "https://media.ccc.de/v/c4.openchaos.2025.11.autoritaere-high-tech-komplex-unterwanderung-der-demokratie, c4.openchaos.2025.11.autoritaere-high-tech-komplex-unterwanderung-der-demokratie",
        )
        fun `event show routes`(
            url: String,
            slug: String,
        ) {
            assertEquals(DeepLink.Events.Show(slug), match(url))
        }
    }

    @Nested
    inner class ConferenceRoutesTest {
        @ParameterizedTest
        @CsvSource(
            "https://media.ccc.de/c/39c3, 39c3",
            "https://media.ccc.de/c/eth0_2025, eth0_2025",
            "https://media.ccc.de/c/nook25, nook25",
            "https://media.ccc.de/c/oc, oc",
            "https://media.ccc.de/c/denog17, denog17",
            "https://media.ccc.de/c/dgna, dgna",
            "https://media.ccc.de/c/god2025, god2025",
            "https://media.ccc.de/c/godotfest2025, godotfest2025",
        )
        fun `conference show routes`(
            url: String,
            acronym: String,
        ) {
            assertEquals(DeepLink.Conferences.Show(acronym), match(url))
        }
    }

    @Nested
    inner class ListRoutesTest {
        @Test
        fun recent() {
            assertEquals(DeepLink.Recent, match("https://media.ccc.de/recent"))
        }

        @Test
        fun popular() {
            assertEquals(DeepLink.Popular, match("https://media.ccc.de/popular"))
        }

        @Test
        fun `browse root`() {
            assertEquals(
                DeepLink.Conferences.Browse,
                match("https://media.ccc.de/b"),
            )
        }

        @Test
        fun `all conferences`() {
            assertEquals(
                DeepLink.Conferences.All,
                match("https://media.ccc.de/a"),
            )
        }
    }

    @Nested
    inner class ExternalDomainTest {
        @ParameterizedTest
        @ValueSource(
            strings = [
                "https://ccc.de/",
                "https://ccc.de/en/imprint",
                "https://c3voc.de/",
            ],
        )
        fun `external domains are ignored`(url: String) {
            assertNull(match(url))
        }
    }
}
