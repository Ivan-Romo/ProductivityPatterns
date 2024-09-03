package com.example.productivitypatterns.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
)

private val LightColorScheme = lightColorScheme(
    primary =  Color(0xFF3343E0),
    secondary = Color.Yellow,
    secondaryContainer = Color(0xFF3343E0), //fondo de los iconos
    surfaceContainer = Color.Red,
    primaryContainer = Color.Yellow,
    onSurfaceVariant = Color.Black, //iconos sin seleccionar y texto de dropdown unfocused
    background =Color(0xFFF5F6FD),
    surface = Color(0xFFFFFBFE),
    tertiaryContainer = Color.Yellow,
)

@Composable
fun ProductivityPatternsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}