package com.crstlnz.utils

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*


open class AnimeSailEmbed() : ExtractorApi() {
    override val mainUrl: String = "https://pixeldrain.com"
    override val name: String = "AnimeSailEmbed"
    override val requiresReferer: Boolean = true

    override suspend fun getUrl(
        url: String,
        referer: String?,
        subtitleCallback: (SubtitleFile) -> Unit,
        callback: (ExtractorLink) -> Unit
    ) {
        val document = app.get(url).document
        val src = document.selectFirst("video source")?.attr("src") ?: return
        callback.invoke(
            ExtractorLink(
                name,
                name,
                src,
                referer ?: url,
                Qualities.Unknown.value
            )
        )
    }
}