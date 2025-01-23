package com.crstlnz

import com.crstlnz.utils.getLastNumber
import com.crstlnz.utils.isGofile
import com.crstlnz.utils.toEmbedUrl
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.extractors.Gofile
import com.lagradost.cloudstream3.utils.*
import org.jsoup.nodes.Document
import java.util.ArrayList


class Anoboy : MainAPI() {
    override var mainUrl = "https://tv1.anoboy.app"
    override var name = "Anoboy"
    override val hasMainPage = true
    override var lang = "id"
    override val hasQuickSearch = true
    override val hasDownloadSupport = true

    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA
    )

    companion object {
        fun getType(t: String): TvType {
            return if (t.contains("OVA", true) || t.contains("Special", true)) TvType.OVA
            else if (t.contains("Movie", true)) TvType.AnimeMovie
            else TvType.Anime
        }

        fun getStatus(t: String): ShowStatus {
            return when (t) {
                "Completed" -> ShowStatus.Completed
                "Ongoing" -> ShowStatus.Ongoing
                else -> ShowStatus.Completed
            }
        }
    }

    override val mainPage = mainPageOf(
        "/category/anime" to "Episode Baru",
        "/category/anime/ongoing" to "Ongoing",
        "/category/anime/ongoing" to "Ongoing",
        "/category/anime-movie" to "Anime Movie",
        "/category/live-action-movie" to "Live Action"
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val home = app.get(
            "$mainUrl${request.data}/page/$page",
        ).document.toSearchResponses() ?: throw ErrorLoadingException("Invalid Json reponse")
        return newHomePageResponse(request.name, home)
    }

    private fun Document.toSearchResponses(): List<SearchResponse> {
        return select(".column-content").map { el ->
            return el.select("a").map { animeEl ->
                newAnimeSearchResponse(
                    animeEl.selectFirst(".ibox1")?.text() ?: "Error",
                    animeEl.attr("href"),
                    TvType.TvSeries,
                ) {
                    this.posterUrl =
                        "$mainUrl${animeEl.selectFirst("amp-img")?.attr("src")?.convertHD()}"
                }
            }
        }
    }

    private fun String.convertHD(): String {
        return replace(Regex("s\\d+"), "s600")
    }

    private fun Document.isWatchPage(): Boolean {
        return selectFirst(".singlelink .lcp_catlist") == null
    }

    private fun Anime.toSearchResponse(): SearchResponse? {

        return newAnimeSearchResponse(
            postTitle ?: return null,
            "$mainUrl/anime/$postName",
            TvType.TvSeries,
        ) {
            this.posterUrl = "$mainUrl/$image"
            addSub(totalEpisode?.toIntOrNull())
        }
    }

    override suspend fun quickSearch(query: String): List<SearchResponse> = search(query)

    override suspend fun search(query: String): List<SearchResponse> {
        return app.get(
            "$mainUrl/category/anime?s=$query",
            referer = "$mainUrl/search/?keyword=$query",
            headers = mapOf("X-Requested-With" to "XMLHttpRequest")
        ).document.toSearchResponses() ?: throw ErrorLoadingException("Invalid reponse")

    }

    override suspend fun load(url: String): LoadResponse {
        val document = app.get(url).document

        val title = document.selectFirst(".entry-title")?.text().toString()
            .replace("Subtitle Indonesia", "").trim()
        val poster = document.selectFirst(".column-three-fourth > amp-img")?.attr("src")
        val tags = document.select(".unduhan table tr").find { it.select("th").text() == "Genre" }
            ?.selectFirst("td")?.text()?.split(", ")
        val type =
            if (document.selectFirst("div.unduhan > div:nth-child(1) > div:nth-child(2) > p > strong")
                    ?.text()?.lowercase() == "live action"
            ) "live action" else "tv"

        val status = if (document.select(".itemListElement")
                .find { it.text().trim().lowercase() == "episode ongoing" } != null
        ) ShowStatus.Ongoing else ShowStatus.Completed
        val description = document.selectFirst(".column-three-fourth .unduhan")?.text()
        val episodes = mutableListOf<Episode>()
        if (document.isWatchPage()) {
            episodes.add(Episode(url, title))
        } else {
            val eps = document.select(".singlelink ul li").map {
                Episode(
                    (it.selectFirst("a")?.attr("href") ?: "").toString(),
                    episode = it.ownText().getLastNumber()
                )
            }
            episodes.addAll(eps)
        }

        return newAnimeLoadResponse(title, url, getType(type)) {
            engName = title
            posterUrl = poster
//            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            plot = description
            this.tags = tags
        }
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        println("Load url $data")
        val document = app.get(data).document
        val providers = document.select(".download p span.ud")
        val map = mutableSetOf<String>()
        for (provider in providers) {
            val links = provider.select("a")
            for (link in links) {
                val url = link.attr("href")
                if (!map.contains(url)) {
                    map.add(url)
                    loadExtractor(
                        url.toEmbedUrl(),
                        mainUrl,
                        subtitleCallback,
                        callback
                    )
                }
            }
        }
        return true
    }

    data class Streamsb(
        @JsonProperty("link") val link: String?,
    )

    data class Server(
        @JsonProperty("streamsb") val streamsb: Streamsb?,
    )

    data class Sources(
        @JsonProperty("server") val server: Server?,
    )

    data class Responses(
        @JsonProperty("data") val data: ArrayList<Anime>? = arrayListOf(),
    )

    data class Anime(
        @JsonProperty("post_title") val postTitle: String?,
        @JsonProperty("post_name") val postName: String?,
        @JsonProperty("image") val image: String?,
        @JsonProperty("total_episode") val totalEpisode: String?,
        @JsonProperty("salt") val salt: String?,
    )

}