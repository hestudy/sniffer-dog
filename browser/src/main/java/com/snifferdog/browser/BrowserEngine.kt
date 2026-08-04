package com.snifferdog.browser

import android.content.Context
import android.view.View

/**
 * Minimal browser engine surface used by the app UI.
 * GeckoView implements this in the `gecko` flavor; `homeDemo` uses a lightweight stub.
 */
interface BrowserEngine {
    fun warmUp()
    fun createSession(): BrowserSession
    fun createView(context: Context): View
    fun attach(view: View, session: BrowserSession)
    fun shutdown()
}
