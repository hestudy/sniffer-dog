"use strict";

/**
 * Built-in GeckoView extension — observe requests and push to native.
 * Native app name must match SnifferExtensionBridge.NATIVE_APP.
 */
let port = null;

function connect() {
  try {
    port = browser.runtime.connectNative("snifferDog");
    port.onDisconnect.addListener(() => {
      port = null;
    });
  } catch (e) {
    console.error("snifferDog native connect failed", e);
  }
}

connect();

function headersToObject(headers) {
  const out = {};
  if (!headers) return out;
  for (const h of headers) {
    out[h.name] = h.value;
  }
  return out;
}

function emit(details) {
  if (!port) connect();
  if (!port) return;
  try {
    port.postMessage({
      type: "request",
      url: details.url,
      method: details.method,
      resourceType: details.type,
      headers: headersToObject(details.requestHeaders),
    });
  } catch (e) {
    console.error("postMessage failed", e);
  }
}

browser.webRequest.onBeforeSendHeaders.addListener(
  emit,
  { urls: ["<all_urls>"] },
  ["requestHeaders"]
);
