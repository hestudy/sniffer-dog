package com.snifferdog.sniffer.model

data class SniffedResource(
    val id: String,
    val url: String,
    val type: ResourceType,
    val method: String = "GET",
    val mimeType: String? = null,
    val contentLength: Long? = null,
    val requestHeaders: Map<String, String> = emptyMap(),
    val timestampMs: Long = System.currentTimeMillis(),
)
