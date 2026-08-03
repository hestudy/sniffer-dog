package com.snifferdog.sniffer

import com.snifferdog.sniffer.model.SniffedResource

/**
 * Builds a pasteable cURL from a sniffed request.
 * ponytail: no shell-escaping matrix for every locale; upgrade if we hit exotic header values.
 */
object CurlBuilder {
    fun build(resource: SniffedResource): String {
        val parts = mutableListOf("curl")
        if (!resource.method.equals("GET", ignoreCase = true)) {
            parts += "-X"
            parts += resource.method.uppercase()
        }
        resource.requestHeaders.forEach { (name, value) ->
            if (name.equals("content-length", ignoreCase = true)) return@forEach
            parts += "-H"
            parts += "'${name}: ${value.replace("'", "'\\''")}'"
        }
        parts += "'${resource.url.replace("'", "'\\''")}'"
        return parts.joinToString(" ")
    }
}
