package com.snifferdog.app.ui.browser

import android.content.Context
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snifferdog.browser.BrowserEngine
import com.snifferdog.browser.BrowserSession
import com.snifferdog.browser.GeckoBrowserEngine
import com.snifferdog.sniffer.SnifferRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val engine: BrowserEngine,
    private val snifferRepository: SnifferRepository,
) : ViewModel() {

    private val geckoEngine: GeckoBrowserEngine
        get() = engine as GeckoBrowserEngine

    private var session: BrowserSession? = null

    var drawerOpen by mutableStateOf(false)
        private set

    val resourceCount = snifferRepository.state
        .map { it.count }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun openSession(url: String): BrowserSession {
        snifferRepository.clear()
        engine.warmUp()
        val created = engine.createSession().also {
            session = it
            it.loadUrl(url)
        }
        return created
    }

    fun createBrowserView(context: Context): View = geckoEngine.newGeckoView(context)

    fun attachSession(view: View, session: BrowserSession) {
        geckoEngine.attach(view, session)
    }

    fun releaseSession() {
        session?.close()
        session = null
        drawerOpen = false
    }

    fun toggleDrawer() {
        drawerOpen = !drawerOpen
    }

    override fun onCleared() {
        releaseSession()
        super.onCleared()
    }
}
