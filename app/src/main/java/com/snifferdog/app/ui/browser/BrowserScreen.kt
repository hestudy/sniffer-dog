package com.snifferdog.app.ui.browser

import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snifferdog.ui.theme.SnifferColors
import com.snifferdog.ui.theme.SnifferTokens

@Composable
fun BrowserScreen(
    targetUrl: String,
    onClose: () -> Unit,
    viewModel: BrowserViewModel = hiltViewModel(),
) {
    val count by viewModel.resourceCount.collectAsStateWithLifecycle()
    val session = remember(targetUrl) { viewModel.openSession(targetUrl) }

    DisposableEffect(Unit) {
        onDispose { viewModel.releaseSession() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnifferColors.Background),
    ) {
        AndroidView(
            factory = { context ->
                viewModel.createBrowserView(context).also { view ->
                    view.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    viewModel.attachSession(view, session)
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

        Text(
            text = "✕",
            color = SnifferColors.Text,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(SnifferColors.Surface, CircleShape)
                .clickable(onClick = onClose)
                .padding(12.dp),
        )

        FloatingActionButton(
            onClick = { viewModel.toggleDrawer() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(SnifferTokens.FabSize),
            containerColor = SnifferColors.Surface,
            contentColor = SnifferColors.Primary,
        ) {
            Text(text = count.toString(), color = SnifferColors.Accent)
        }

        if (viewModel.drawerOpen) {
            Text(
                text = "Resource Drawer ($count) — skeleton",
                color = SnifferColors.Text,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 96.dp)
                    .background(SnifferColors.Surface)
                    .padding(16.dp),
            )
        }
    }
}
