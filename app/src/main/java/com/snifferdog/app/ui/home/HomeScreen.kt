package com.snifferdog.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.snifferdog.ui.theme.SnifferColors
import com.snifferdog.ui.theme.SnifferTokens

@Composable
fun HomeScreen(
    onSniff: (String) -> Unit,
) {
    var url by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SnifferColors.Background)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        SnifferColors.Primary.copy(alpha = 0.18f),
                        SnifferColors.Accent.copy(alpha = 0.12f),
                        Color.Transparent,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Sniffer Dog",
                color = SnifferColors.Text,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "X-ray the web. Extract hidden media.",
                color = SnifferColors.Muted,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp),
                textAlign = TextAlign.Center,
            )
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SnifferTokens.InputHeight),
                placeholder = { Text("Enter target URL…", color = SnifferColors.Muted) },
                singleLine = true,
                shape = RoundedCornerShape(percent = 50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SnifferColors.Primary,
                    unfocusedBorderColor = SnifferColors.Border,
                    focusedContainerColor = SnifferColors.Surface,
                    unfocusedContainerColor = SnifferColors.Surface,
                    focusedTextColor = SnifferColors.Text,
                    unfocusedTextColor = SnifferColors.Text,
                    cursorColor = SnifferColors.Primary,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Go,
                ),
                keyboardActions = KeyboardActions(
                    onGo = { submit(url, onSniff) },
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { submit(url, onSniff) },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(40.dp)
                            .background(SnifferColors.Primary, CircleShape),
                    ) {
                        Text("→", color = SnifferColors.Background)
                    }
                },
            )
        }
    }
}

private fun submit(raw: String, onSniff: (String) -> Unit) {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return
    val normalized = when {
        trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
        else -> "https://$trimmed"
    }
    onSniff(normalized)
}
