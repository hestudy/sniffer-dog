package com.snifferdog.ui.components

import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.snifferdog.ui.theme.SnifferColors

/** Ambient cyan / purple mesh blobs from the Home Entry design. */
@Composable
fun AmbientMeshBackground(
    modifier: Modifier = Modifier,
) {
    val transition = rememberInfiniteTransition(label = "mesh")
    val blobA by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "blobA",
    )
    val blobB by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "blobB",
    )
    val blobC by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(14_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "blobC",
    )

    val config = LocalConfiguration.current
    val w = config.screenWidthDp.dp
    val h = config.screenHeightDp.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SnifferColors.Background),
    ) {
        MeshBlob(
            color = SnifferColors.Primary,
            size = 300.dp,
            alpha = 0.22f,
            blurRadius = 100.dp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(
                    x = w * 0.05f + 30.dp * blobA,
                    y = h * 0.15f - 40.dp * blobA,
                ),
        )
        MeshBlob(
            color = SnifferColors.Accent,
            size = 250.dp,
            alpha = 0.22f,
            blurRadius = 100.dp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(
                    x = (-w * 0.05f) - 30.dp * blobB,
                    y = (-h * 0.12f) + 40.dp * blobB,
                ),
        )
        MeshBlob(
            color = SnifferColors.Primary,
            size = 400.dp,
            alpha = 0.12f,
            blurRadius = 120.dp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(
                    x = 24.dp * (blobC - 0.5f),
                    y = (-24).dp * (blobC - 0.5f),
                ),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            SnifferColors.Background.copy(alpha = 0.4f),
                        ),
                    ),
                ),
        )
    }
}

@Composable
private fun MeshBlob(
    color: Color,
    size: Dp,
    alpha: Float,
    blurRadius: Dp,
    modifier: Modifier = Modifier,
) {
    val soft = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Modifier.blur(blurRadius)
    } else {
        // Pre-12: fake bloom with a larger, softer circle.
        Modifier
    }
    Box(
        modifier = modifier
            .size(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) size else size * 1.35f)
            .then(soft)
            .background(color.copy(alpha = alpha), CircleShape),
    )
}
