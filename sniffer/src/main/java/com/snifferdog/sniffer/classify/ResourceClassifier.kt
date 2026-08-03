package com.snifferdog.sniffer.classify

import com.snifferdog.sniffer.model.ResourceType

object ResourceClassifier {
    private val imageExt = setOf("png", "jpg", "jpeg", "gif", "webp", "svg", "bmp", "ico", "avif")
    private val audioExt = setOf("mp3", "wav", "ogg", "m4a", "aac", "flac", "opus")
    private val videoExt = setOf("mp4", "webm", "mkv", "mov", "m3u8", "mpd", "ts")

    fun classify(url: String, mimeType: String?, resourceTypeHint: String? = null): ResourceType {
        hintToType(resourceTypeHint)?.let { return it }
        mimeType?.lowercase()?.let { mime ->
            when {
                mime.startsWith("image/") -> return ResourceType.IMAGE
                mime.startsWith("audio/") -> return ResourceType.AUDIO
                mime.startsWith("video/") -> return ResourceType.VIDEO
                mime.contains("mpegurl") || mime.contains("dash+xml") -> return ResourceType.VIDEO
            }
        }
        val ext = url.substringBefore('#').substringBefore('?')
            .substringAfterLast('.', missingDelimiterValue = "")
            .lowercase()
        return when (ext) {
            in imageExt -> ResourceType.IMAGE
            in audioExt -> ResourceType.AUDIO
            in videoExt -> ResourceType.VIDEO
            else -> ResourceType.OTHER
        }
    }

    private fun hintToType(hint: String?): ResourceType? = when (hint?.lowercase()) {
        "image", "images" -> ResourceType.IMAGE
        "media", "audio" -> ResourceType.AUDIO // webRequest "media" is often A/V; refine later
        "video", "xmlhttprequest", "other" -> null
        else -> null
    }
}
