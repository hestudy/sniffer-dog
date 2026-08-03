package com.snifferdog.browser

import android.content.Context
import android.view.View
import com.snifferdog.sniffer.SnifferRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView

/**
 * GeckoView-backed engine. Installs the built-in resource-sniffer extension on warm-up.
 */
@Singleton
class GeckoBrowserEngine @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val snifferRepository: SnifferRepository,
    private val extensionBridge: SnifferExtensionBridge,
) : BrowserEngine {

    @Volatile
    private var runtime: GeckoRuntime? = null

    override fun warmUp() {
        if (runtime != null) return
        val settings = GeckoRuntimeSettings.Builder()
            .javaScriptEnabled(true)
            .build()
        val rt = GeckoRuntime.create(context, settings)
        runtime = rt
        extensionBridge.install(rt)
    }

    override fun createSession(): BrowserSession {
        warmUp()
        val session = GeckoSession()
        session.open(requireNotNull(runtime))
        return GeckoBrowserSession(session)
    }

    override fun createView(context: Context): View {
        warmUp()
        return GeckoView(context)
    }

    override fun attach(view: View, session: BrowserSession) {
        val geckoView = view as GeckoView
        val geckoSession = (session as GeckoBrowserSession).geckoSession
        geckoView.setSession(geckoSession)
    }

    override fun shutdown() {
        runtime?.shutdown()
        runtime = null
        snifferRepository.clear()
    }

    private class GeckoBrowserSession(
        val geckoSession: GeckoSession,
        override val id: String = UUID.randomUUID().toString(),
    ) : BrowserSession {
        override fun loadUrl(url: String) {
            geckoSession.loadUri(url)
        }

        override fun stop() {
            geckoSession.stop()
        }

        override fun close() {
            geckoSession.close()
        }
    }
}
