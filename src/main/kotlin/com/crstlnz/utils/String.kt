package com.crstlnz.utils

import java.net.URL

fun String.getLastNumber(): Int? {
    val regex = "\\d+".toRegex()
    val matches = regex.findAll(this)
    return matches.lastOrNull()?.value?.toInt()
}

fun String.isGofile(): Boolean {
    return try {
        val host = URL(this).host
        host.equals("gofile.io", ignoreCase = true)
    } catch (e: Exception) {
        false // Return false if the URL is malformed
    }
}

fun String.isMp4upload(): Boolean {
    return contains("mp4upload.com", ignoreCase = true)
}

fun String.toEmbedUrl(): String {
    val parts = this.split("/")
    if (isMp4upload()) {
        val videoId = parts.last()
        return "https://www.mp4upload.com/embed-$videoId.html"
    }
    return this
}