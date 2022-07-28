package de.stuermerbenjamin.ble.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Yellow200 = Color(0xffffeb46)
private val Blue200 = Color(0xff91a4fc)
private val DarkPrimaryColor = Color(0xff5d4037)
private val DarkAccentColor = Color(0xff607d8b)

private val DarkColorPalette = darkColors(
    primary = Yellow200,
    secondary = Blue200,
)
private val LightColorPalette = lightColors(
    primary = DarkPrimaryColor,
    secondary = DarkAccentColor,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) LightColorPalette else DarkColorPalette

    MaterialTheme(
        colors = colors,
        content = content
    )
}
