package com.snifferdog.browser

/**
 * One loaded tab / page. UI hosts the native view separately via [GeckoBrowserEngine.attach].
 */
interface BrowserSession {
    val id: String
    fun loadUrl(url: String)
    fun stop()
    fun close()
}
