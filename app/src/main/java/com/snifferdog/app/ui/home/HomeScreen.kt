package com.snifferdog.app.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.snifferdog.ui.components.AmbientMeshBackground
import com.snifferdog.ui.components.RadarIcon
import com.snifferdog.ui.theme.DmSans
import com.snifferdog.ui.theme.SnifferColors
import com.snifferdog.ui.theme.SnifferTokens
import com.snifferdog.ui.theme.SpaceGrotesk
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onSniff: (String) -> Unit,
) {
    var url by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    val headerAlpha by animateFloatAsState(
        targetValue = if (loading) 0f else 0.8f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "headerAlpha",
    )
    val headerOffset by animateDpAsState(
        targetValue = if (loading) (-20).dp else 0.dp,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "headerOffset",
    )

    fun submit() {
        val trimmed = url.trim()
        if (trimmed.isEmpty() || loading) return
        val normalized = when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            else -> "https://$trimmed"
        }
        focusManager.clearFocus()
        loading = true
        scope.launch {
            delay(900)
            onSniff(normalized)
            loading = false
            url = ""
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AmbientMeshBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(headerAlpha)
                    .offset(y = headerOffset),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    RadarIcon(size = 30.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sniffer",
                        style = TextStyle(
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            letterSpacing = (-0.8).sp,
                            color = SnifferColors.Text,
                        ),
                    )
                }
                Text(
                    text = "Fluid Media Extractor",
                    style = TextStyle(
                        fontFamily = DmSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = SnifferColors.Muted,
                    ),
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            GlassUrlBar(
                value = url,
                onValueChange = { if (!loading) url = it },
                loading = loading,
                onSubmit = { submit() },
            )
        }
    }
}

@Composable
private fun GlassUrlBar(
    value: String,
    onValueChange: (String) -> Unit,
    loading: Boolean,
    onSubmit: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val focused by interaction.collectIsFocusedAsState()

    val pulse = rememberInfiniteTransition(label = "pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 0.98f,
        animationSpec = infiniteRepeatable(
            animation = tween(750),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseScale",
    )
    val pulseGlow by pulse.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(750),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulseGlow",
    )

    val borderColor = when {
        loading -> SnifferColors.Primary.copy(alpha = 0.55f)
        focused -> SnifferColors.Primary.copy(alpha = 0.5f)
        else -> SnifferColors.Border
    }
    val glowAlpha = when {
        loading -> pulseGlow
        focused -> 0.30f
        else -> 0f
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        val expandedWidth = min(maxWidth, 400.dp)
        val barWidth by animateDpAsState(
            targetValue = if (loading) 80.dp else expandedWidth,
            animationSpec = tween(500, easing = FastOutSlowInEasing),
            label = "barWidth",
        )

        Box(
            modifier = Modifier
                .width(barWidth)
                .height(SnifferTokens.InputHeight)
                .then(if (loading) Modifier.scale(pulseScale) else Modifier)
                .clip(CircleShape)
                .background(SnifferColors.Surface, CircleShape)
                .border(1.dp, borderColor, CircleShape)
                .then(
                    if (glowAlpha > 0f) {
                        Modifier.border(
                            width = 1.5.dp,
                            color = SnifferColors.Primary.copy(alpha = glowAlpha),
                            shape = CircleShape,
                        )
                    } else {
                        Modifier
                    },
                )
                .padding(6.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (!loading) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp, end = 8.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "Enter target URL...",
                                style = TextStyle(
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 16.sp,
                                    color = SnifferColors.Muted.copy(alpha = 0.85f),
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                color = SnifferColors.Text,
                                textAlign = TextAlign.Center,
                            ),
                            cursorBrush = SolidColor(SnifferColors.Primary),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Uri,
                                imeAction = ImeAction.Go,
                            ),
                            keyboardActions = KeyboardActions(onGo = { onSubmit() }),
                            interactionSource = interaction,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SnifferColors.Primary)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                role = Role.Button,
                                onClick = onSubmit,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Sniff",
                            tint = SnifferColors.Background,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                }
            }
        }
    }
}
