package com.snifferdog.sniffer.classify

import com.snifferdog.sniffer.model.ResourceType
import org.junit.Assert.assertEquals
import org.junit.Test

class ResourceClassifierTest {
    @Test
    fun classifiesByExtension() {
        assertEquals(ResourceType.IMAGE, ResourceClassifier.classify("https://a/b.png", null))
        assertEquals(ResourceType.AUDIO, ResourceClassifier.classify("https://a/b.mp3?x=1", null))
        assertEquals(ResourceType.VIDEO, ResourceClassifier.classify("https://a/b.m3u8", null))
    }

    @Test
    fun mimeOverridesExtension() {
        assertEquals(
            ResourceType.VIDEO,
            ResourceClassifier.classify("https://a/token", "video/mp4"),
        )
    }
}
