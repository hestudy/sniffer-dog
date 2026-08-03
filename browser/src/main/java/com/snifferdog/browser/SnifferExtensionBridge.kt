package com.snifferdog.browser

import android.util.Log
import com.snifferdog.sniffer.SnifferRepository
import javax.inject.Inject
import javax.inject.Singleton
import org.json.JSONObject
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.WebExtension

/**
 * Installs assets/extensions/resource-sniffer and forwards webRequest events into [SnifferRepository].
 */
@Singleton
class SnifferExtensionBridge @Inject constructor(
    private val snifferRepository: SnifferRepository,
) {
    fun install(runtime: GeckoRuntime) {
        runtime.webExtensionController
            .ensureBuiltIn(EXTENSION_RESOURCE, EXTENSION_ID)
            .accept(
                { extension ->
                    if (extension == null) {
                        Log.e(TAG, "ensureBuiltIn returned null")
                        return@accept
                    }
                    Log.i(TAG, "Installed $EXTENSION_ID")
                    bindMessaging(extension)
                },
                { error -> Log.e(TAG, "Failed to install sniff extension", error) },
            )
    }

    private fun bindMessaging(extension: WebExtension) {
        val delegate = object : WebExtension.MessageDelegate {
            override fun onMessage(
                nativeApp: String,
                message: Any,
                sender: WebExtension.MessageSender,
            ): GeckoResult<Any>? {
                handleMessage(message)
                return null
            }
        }
        extension.setMessageDelegate(delegate, NATIVE_APP)
    }

    private fun handleMessage(message: Any) {
        val json = message as? JSONObject ?: return
        when (json.optString("type")) {
            "request" -> {
                snifferRepository.ingest(
                    url = json.optString("url"),
                    method = json.optString("method", "GET"),
                    mimeType = json.optString("mimeType").ifEmpty { null },
                    contentLength = json.optLong("contentLength", -1L).takeIf { it >= 0 },
                    requestHeaders = json.optJSONObject("headers")?.let { headers ->
                        buildMap {
                            headers.keys().forEach { key -> put(key, headers.optString(key)) }
                        }
                    } ?: emptyMap(),
                    resourceTypeHint = json.optString("resourceType").ifEmpty { null },
                )
            }
        }
    }

    companion object {
        private const val TAG = "SnifferExt"
        const val EXTENSION_ID = "resource-sniffer@snifferdog.com"
        const val EXTENSION_RESOURCE = "resource://android/assets/extensions/resource-sniffer/"
        const val NATIVE_APP = "snifferDog"
    }
}
