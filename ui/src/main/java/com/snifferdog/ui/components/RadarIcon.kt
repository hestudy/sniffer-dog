package com.snifferdog.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.snifferdog.ui.theme.SnifferColors

/** Filled radar mark matching Material Symbol "radar" in the design. */
@Composable
fun RadarIcon(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    tint: Color = SnifferColors.Primary,
) {
    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(width = size.toPx() * 0.08f)
        val c = Offset(this.size.width / 2f, this.size.height / 2f)
        val r = this.size.minDimension / 2f
        drawCircle(color = tint, radius = r * 0.18f, center = c)
        drawCircle(color = tint, radius = r * 0.42f, center = c, style = stroke)
        drawCircle(color = tint, radius = r * 0.72f, center = c, style = stroke)
        // Sweep wedge
        drawArc(
            color = tint.copy(alpha = 0.35f),
            startAngle = -90f,
            sweepAngle = 70f,
            useCenter = true,
            topLeft = Offset(c.x - r * 0.72f, c.y - r * 0.72f),
            size = androidx.compose.ui.geometry.Size(r * 1.44f, r * 1.44f),
        )
    }
}
