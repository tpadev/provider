package com.crstlnz.utils

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.*


open class AnimeSailEmbed() : ExtractorApi() {
    override val mainUrl: String = "https://pixeldrain.com"
    override val name: String = "AnimeSailEmbed"
    override val requiresReferer: Boolean = true

    companion object {
        private fun removeResolution(input: String): String {
            // Use a regular expression to match resolution patterns like 720p, 1080p, 360p, etc.
            val resolutionRegex = "\\b\\d{3,4}p\\b".toRegex()
            // Replace the matched resolutions with an empty string and trim the result
            return resolutionRegex.replace(input, "").trim()
        }

        fun getSource(str: String): String {
            if (str.contains("pixel")) {
                return "Pixeldrain"
            } else if (str.contains("acefile")) {
                return "Acefile"
            } else if (str.contains("mp4")) {
                return "MP4Upload"
            } else if (str.contains("pompom")) {
                return "pomf2"
            } else if (str.contains("kraken")) {
                return "Krakenfiles"
            } else if (str.contains("kowo")){
                return "Qiwi"
            }

            return removeResolution(str)
        }
    }


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
