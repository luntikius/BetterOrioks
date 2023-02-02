package com.example.betterorioks.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = gray300,
    primaryVariant = gray200,
    secondary = bright_blue,
    background = gray100,
    onBackground = black,
    surface = white,
    onSurface = black,
    onPrimary = black
)

@SuppressLint("ConflictingOnColor")
private val DarkColorPalette = darkColors(
    primary = gray300,
    primaryVariant = gray600,
    secondary = bright_blue,
    background = gray800,
    onBackground = white,
    surface = gray500,
    onSurface = white,
    onPrimary = white
)

@Composable
fun BetterOrioksTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}