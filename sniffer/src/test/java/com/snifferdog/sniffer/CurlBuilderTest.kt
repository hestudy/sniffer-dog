package com.snifferdog.sniffer

import com.snifferdog.sniffer.model.ResourceType
import com.snifferdog.sniffer.model.SniffedResource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CurlBuilderTest {
    @Test
    fun buildsGetWithHeaders() {
        val resource = SniffedResource(
            id = "1",
            url = "https://cdn.example.com/a.mp4",
            type = ResourceType.VIDEO,
            method = "GET",
            requestHeaders = mapOf(
                "User-Agent" to "SnifferDog/0.1",
                "Referer" to "https://example.com/",
            ),
        )
        val curl = CurlBuilder.build(resource)
        assertTrue(curl.startsWith("curl "))
        assertTrue(curl.contains("-H 'User-Agent: SnifferDog/0.1'"))
        assertTrue(curl.contains("-H 'Referer: https://example.com/'"))
        assertTrue(curl.endsWith("'https://cdn.example.com/a.mp4'"))
    }

    @Test
    fun usesMethodFlagWhenNotGet() {
        val resource = SniffedResource(
            id = "2",
            url = "https://example.com/x",
            type = ResourceType.IMAGE,
            method = "HEAD",
        )
        assertEquals(
            "curl -X HEAD 'https://example.com/x'",
            CurlBuilder.build(resource),
        )
    }
}
