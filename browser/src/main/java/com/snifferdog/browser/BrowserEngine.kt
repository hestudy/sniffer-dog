package com.snifferdog.browser

/**
 * Minimal browser engine surface used by the app UI.
 * GeckoView implements this; keeps UI free of Mozilla types.
 */
interface BrowserEngine {
    fun warmUp()
    fun createSession(): BrowserSession
    fun shutdown()
}
