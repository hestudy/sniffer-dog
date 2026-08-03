package com.snifferdog.sniffer

import com.snifferdog.sniffer.classify.ResourceClassifier
import com.snifferdog.sniffer.model.ResourceType
import com.snifferdog.sniffer.model.SniffedResource
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SnifferSessionState(
    val resources: List<SniffedResource> = emptyList(),
) {
    val count: Int get() = resources.size
    fun byType(type: ResourceType): List<SniffedResource> = resources.filter { it.type == type }
}

/**
 * In-memory store for one browse session. Fed by the Gecko webRequest bridge.
 */
@Singleton
class SnifferRepository @Inject constructor() {
    private val seenUrls = ConcurrentHashMap.newKeySet<String>()
    private val _state = MutableStateFlow(SnifferSessionState())
    val state: StateFlow<SnifferSessionState> = _state.asStateFlow()

    fun clear() {
        seenUrls.clear()
        _state.value = SnifferSessionState()
    }

    fun ingest(
        url: String,
        method: String = "GET",
        mimeType: String? = null,
        contentLength: Long? = null,
        requestHeaders: Map<String, String> = emptyMap(),
        resourceTypeHint: String? = null,
    ): SniffedResource? {
        val type = ResourceClassifier.classify(url, mimeType, resourceTypeHint)
        if (type == ResourceType.OTHER) return null
        if (!seenUrls.add(url)) return null

        val resource = SniffedResource(
            id = UUID.randomUUID().toString(),
            url = url,
            type = type,
            method = method,
            mimeType = mimeType,
            contentLength = contentLength,
            requestHeaders = requestHeaders,
        )
        _state.update { it.copy(resources = it.resources + resource) }
        return resource
    }
}
