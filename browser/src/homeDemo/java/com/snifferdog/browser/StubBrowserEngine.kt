package com.snifferdog.browser

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.snifferdog.sniffer.SnifferRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Lightweight stub for emulator / home-UI demos without the ~200MB GeckoView AAR.
 */
@Singleton
class StubBrowserEngine @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val snifferRepository: SnifferRepository,
) : BrowserEngine {

    override fun warmUp() = Unit

    override fun createSession(): BrowserSession = StubSession()

    override fun createView(context: Context): View {
        return FrameLayout(context).apply {
            setBackgroundColor(0xFF050914.toInt())
            addView(
                TextView(context).apply {
                    text = "Browser stub (homeDemo)\nGeckoView excluded for light APK"
                    setTextColor(Color.WHITE)
                    gravity = Gravity.CENTER
                    textSize = 14f
                },
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER,
                ),
            )
        }
    }

    override fun attach(view: View, session: BrowserSession) = Unit

    override fun shutdown() {
        snifferRepository.clear()
    }

    private class StubSession(
        override val id: String = UUID.randomUUID().toString(),
    ) : BrowserSession {
        override fun loadUrl(url: String) = Unit
        override fun stop() = Unit
        override fun close() = Unit
    }
}
