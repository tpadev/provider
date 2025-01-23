package com.crstlnz

import com.crstlnz.utils.getLastNumber
import com.crstlnz.utils.loadAnimeSail
import com.crstlnz.utils.splitTitles
import com.crstlnz.utils.toAbsoluteURL
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.AnimeLoadResponse
import com.lagradost.cloudstream3.DubStatus
import com.lagradost.cloudstream3.Episode
import com.lagradost.cloudstream3.ErrorLoadingException
import com.lagradost.cloudstream3.HomePageResponse
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.MainPageRequest
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.ShowStatus
import com.lagradost.cloudstream3.SubtitleFile
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.addEpisodes
import com.lagradost.cloudstream3.addSub
import com.lagradost.cloudstream3.app
import com.lagradost.cloudstream3.mainPageOf
import com.lagradost.cloudstream3.newAnimeLoadResponse
import com.lagradost.cloudstream3.newAnimeSearchResponse
import com.lagradost.cloudstream3.newHomePageResponse
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class AnimeSail : MainAPI() {
    override var mainUrl = "https://154.26.137.28"
    override var name = "AnimeSail"
    override val hasMainPage = true
    override var lang = "id"
    override val hasQuickSearch = true
    override val hasDownloadSupport = true

    override val supportedTypes = setOf(
        TvType.Anime,
        TvType.AnimeMovie,
        TvType.OVA
    )

    init {
        val headers = mutableMapOf<String, String>()
        headers["Cookie"] = "_as_ipin_ct=ID;"
        app.defaultHeaders = headers
    }

    override val mainPage = mainPageOf(
        "/" to "Anime & Donghua",
    )

    override suspend fun getMainPage(
        page: Int,
        request: MainPageRequest
    ): HomePageResponse {
        val home = app.get(
            "$mainUrl${request.data}page/$page?s=",
        ).document.toSearchResponses() ?: throw ErrorLoadingException("Invalid Json reponse")
        return newHomePageResponse(request.name, home)
    }

    private fun Document.toSearchResponses(): List<SearchResponse> {
        return select(".listupd").map { el ->
            return el.select("article").map { animeEl ->
                newAnimeSearchResponse(
                    animeEl.selectFirst(".tt h2")?.text() ?: "Error",
                    (animeEl.selectFirst("a")?.attr("href") ?: "").toAbsoluteURL(mainUrl),
                    getTvType(animeEl.selectFirst(".tt span")?.ownText()),
                ) {
                    this.posterUrl = animeEl.selectFirst("img")?.attr("src")?.toAbsoluteURL(mainUrl)
                        ?.toSmallerImage()
                }
            }
        }
    }

    private fun String.removeYear(): String {
        return replace("\\(\\d{4}\\)".toRegex(), "")
    }

    private fun String.toSmallerImage(): String {
        // return "$this?w=350"
        return this // disable for now
    }

    private fun getTvType(str: String?): TvType {
        if (str == null) return TvType.Anime
        return if (str.contains("OVA", true) || str.contains("Special", true)) {
            TvType.OVA
        } else if (str.contains("ONA", true)) {
            TvType.Others
        } else if (str.contains("Movie", true)) {
            TvType.AnimeMovie
        } else if (str.contains("Live Action", true)) {
            TvType.Movie
        } else {
            TvType.Anime
        }

    }

    private fun Document.isWatchPage(): Boolean {
        return selectFirst(".singlelink .lcp_catlist") == null
    }

    private fun Element.getElementsBefore(cssQuery: String?): Elements {
        // Find the target element using the CSS query
        val targetElement = selectFirst(cssQuery!!)
            ?: return Elements() // Return an empty Elements if target not found

        // Get all siblings of the target element's parent
        val siblings = targetElement.parent()?.children()

        // Create a new Elements object to hold the result
        val beforeElements = Elements()

        // Loop through siblings until we reach the target element
        if (siblings != null) {
            for (sibling in siblings) {
                if (sibling === targetElement) {
                    break // Stop when we find the target element
                }
                beforeElements.add(sibling)
            }
        }

        return beforeElements
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

    override suspend fun load(url: String): AnimeLoadResponse {
        val document = app.get(url).document
        val jTitle = document.selectFirst(".entry-title")?.text().toString()
            .replace("Subtitle Indonesia", "").trim()
        val title = document.getTableContent("Alternatif:")?.splitTitles()?.first
        val poster = document.selectFirst(".entry-content img")?.attr("src")?.toSmallerImage()
        val tags = document.getTableContent("Genre:")?.split(",")?.map { it.trim() }
        val type = document.getTableContent("Tipe:")

        val status = getStatus(document.getTableContent("Status:"))
        val description = document.selectFirst(".entry-content")?.getElementsBefore("h2")?.text()
        val episodes = mutableListOf<Episode>()
        val eps = document.select(".entry-content ul.daftar li").map {
            if (it.text().lowercase().contains("download")) {
                null
            } else {
                Episode(
                    (it.selectFirst("a")?.attr("href") ?: "").toString(),
                    episode = it.text().getLastNumber(),
                    name = it.text().replace("Subtitle Indonesia", "")
                )
            }
        }
        episodes.addAll(eps.filterNotNull())

        return newAnimeLoadResponse(jTitle, url, getTvType(type)) {
            engName = title
            japName = jTitle
            posterUrl = poster
//            this.year = year
            addEpisodes(DubStatus.Subbed, episodes)
            showStatus = status
            plot = description
            this.tags = tags
        }
    }

    private fun Document.getTableContent(title: String): String? {
        return select(".entry-content table tr")
            .find {
                it.selectFirst("th")?.ownText()?.trim()?.lowercase() == title.lowercase()
            }
            ?.selectFirst("td")?.text()?.trim()
    }

    private fun getStatus(status: String?): ShowStatus {
        if (status != null) {
            if (status.contains("Ongoing", true)) {
                return ShowStatus.Ongoing
            }
        }
        return ShowStatus.Completed
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        println("Load url $data")
        val document = app.get(data).document
        val links = document.select(".mobius select option")

        for (linkEl in links) {
            if (linkEl.attr("data-em") == "") continue
            val iframe = String(Base64.decode(linkEl.attr("data-em").toByteArray()))
            val link = Jsoup.parse(iframe).selectFirst("iframe")?.attr("src") ?: ""
            val name = linkEl.text()
            println("Link : $link")
            if (link.contains("154.26.137.28")) {
                loadAnimeSail(link, name, subtitleCallback = subtitleCallback, callback = callback)
            } else {
                loadExtractor(
                    link,
                    mainUrl,
                    subtitleCallback,
                    callback
                )
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