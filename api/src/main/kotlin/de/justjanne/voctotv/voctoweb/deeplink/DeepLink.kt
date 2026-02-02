package de.justjanne.voctotv.voctoweb.deeplink

import de.justjanne.voctotv.voctoweb.deeplink.DeepLink.Conferences.Tag

sealed class DeepLink(
    val url: String,
) {
    data object Index : DeepLink("/") {
        val Regex = "/".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { Index }
    }

    data object About : DeepLink("/about") {
        val Regex = "/about(?:.html)?/?".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { About }
    }

    data object Search : DeepLink("/search") {
        val Regex = "/search(?:.html)?/?".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { Search }
    }

    data object Sitemap : DeepLink("/sitemap.xml") {
        val Regex = "/sitemap.xml/?".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { Sitemap }
    }

    data object Events {
        data class Show(
            val slug: String,
        ) : DeepLink("/v/$slug") {
            companion object {
                val Regex = "/v/(?<slug>[^/]+)/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Show(
                                slug = it.groups["slug"]?.value ?: return null,
                            )
                        }
            }
        }

        data class Postroll(
            val slug: String,
        ) : DeepLink("/postroll/$slug") {
            companion object {
                val Regex = "/postroll/(?<slug>[^/]+)/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Postroll(
                                slug = it.groups["slug"]?.value ?: return null,
                            )
                        }
            }
        }

        data class OEmbed(
            val slug: String,
        ) : DeepLink("/v/$slug/oembed") {
            companion object {
                val Regex = "/v/(?<slug>[^/]+)/oembed/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            OEmbed(
                                slug = it.groups["slug"]?.value ?: return null,
                            )
                        }
            }
        }

        data class Playlist(
            val slug: String,
        ) : DeepLink("/v/$slug/playlist") {
            companion object {
                val Regex = "/v/(?<slug>[^/]+)/playlist/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Playlist(
                                slug = it.groups["slug"]?.value ?: return null,
                            )
                        }
            }
        }

        data class Audio(
            val slug: String,
        ) : DeepLink("/v/$slug/audio") {
            companion object {
                val Regex = "/v/(?<slug>[^/]+)/audio/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Audio(
                                slug = it.groups["slug"]?.value ?: return null,
                            )
                        }
            }
        }

        data class Related(
            val slug: String,
        ) : DeepLink("/v/$slug/related") {
            companion object {
                val Regex = "/v/(?<slug>[^/]+)/related/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Related(
                                slug = it.groups["slug"]?.value ?: return null,
                            )
                        }
            }
        }
    }

    data object Conferences {
        data class Show(
            val acronym: String,
        ) : DeepLink("/c/$acronym") {
            companion object {
                val Regex = "/c/(?<acronym>[^/]+)/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Show(
                                acronym = it.groups["acronym"]?.value ?: return null,
                            )
                        }
            }
        }

        data class Tag(
            val acronym: String,
            val tag: String,
        ) : DeepLink("/c/$acronym/$tag") {
            companion object {
                val Regex = "/c/(?<acronym>[^/]+)/(?<tag>[^/]+)/?".toRegex()

                fun match(url: String) =
                    Regex
                        .matchEntire(url)
                        ?.let {
                            Tag(
                                acronym = it.groups["acronym"]?.value ?: return null,
                                tag = it.groups["tag"]?.value ?: return null,
                            )
                        }
            }
        }

        data object All : DeepLink("/a") {
            val Regex = "/a(?:.html)?/?".toRegex()

            fun match(url: String) = Regex.matchEntire(url)?.let { All }
        }

        data object Browse : DeepLink("/b") {
            val Regex = "/b(?:.html)?/?".toRegex()

            fun match(url: String) = Regex.matchEntire(url)?.let { Browse }
        }
    }

    data object Recent : DeepLink("/recent") {
        val Regex = "/recent(?:.html)?/?".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { Recent }
    }

    data object Popular : DeepLink("/popular") {
        val Regex = "/popular(?:.html)?/?".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { Popular }
    }

    data class PopularByYear(
        val year: String,
    ) : DeepLink("/popular/$year") {
        companion object {
            val Regex = "/popular/(?<year>[^/]+)(?:.html)?/?".toRegex()

            fun match(url: String) =
                Regex
                    .matchEntire(url)
                    ?.let {
                        PopularByYear(
                            year = it.groups["year"]?.value ?: return null,
                        )
                    }
        }
    }

    data object Unpopular : DeepLink("/unpopular") {
        val Regex = "/unpopular(?:.html)?/?".toRegex()

        fun match(url: String) = Regex.matchEntire(url)?.let { Unpopular }
    }

    data class UnpopularByYear(
        val year: String,
    ) : DeepLink("/unpopular/$year") {
        companion object {
            val Regex = "/unpopular/(?<year>[^/]+(?:.html)?)".toRegex()

            fun match(url: String) =
                Regex
                    .matchEntire(url)
                    ?.let {
                        UnpopularByYear(
                            year = it.groups["year"]?.value ?: return null,
                        )
                    }
        }
    }

    data class Tag(
        val tag: String,
    ) : DeepLink("/tags/$tag") {
        companion object {
            val Regex = "/tags/(?<tag>[^/]+)(?:.html)?/?".toRegex()

            fun match(url: String) =
                Regex
                    .matchEntire(url)
                    ?.let {
                        Tag(
                            tag = it.groups["tag"]?.value ?: return null,
                        )
                    }
        }
    }

    companion object {
        fun match(
            host: String,
            path: String,
        ): DeepLink? =
            when (host.lowercase()) {
                "media.ccc.de",
                "media.test.c3voc.de",
                ->
                    Index.match(path)
                        ?: About.match(path)
                        ?: Search.match(path)
                        ?: Sitemap.match(path)
                        ?: Events.Show.match(path)
                        ?: Events.Postroll.match(path)
                        ?: Events.OEmbed.match(path)
                        ?: Events.Playlist.match(path)
                        ?: Events.Audio.match(path)
                        ?: Events.Related.match(path)
                        ?: Conferences.Show.match(path)
                        ?: Conferences.Tag.match(path)
                        ?: Conferences.All.match(path)
                        ?: Conferences.Browse.match(path)
                        ?: Recent.match(path)
                        ?: Popular.match(path)
                        ?: PopularByYear.match(path)
                        ?: Unpopular.match(path)
                        ?: UnpopularByYear.match(path)
                        ?: Tag.match(path)

                else -> null
            }
    }
}
