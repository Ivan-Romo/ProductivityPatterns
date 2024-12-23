package com.productivity.productivitypatterns.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
//    primary = Color(0xFF08A4A7), // Color adaptado del primary en modo claro
//    secondary = Color(0xFFFFD700), // Amarillo, adaptado para destacar en modo oscuro
//    secondaryContainer = Color(0xFFFFFFFF), // Fondo de los iconos adaptado para el modo oscuro
//    surfaceContainer = Color(0xFF8B0000), // Rojo oscuro para mejor contraste en modo oscuro
//    primaryContainer = Color(0xFFFFD700), // Amarillo, como en el modo claro
//    onSurfaceVariant = Color(0xFFFFFFFF), // Blanco para iconos sin seleccionar y texto en modo oscuro
//    background = Color(0xFF121212), // Fondo oscuro típico para el modo oscuro
//    surface = Color(0xFF1E1E1E), // Superficie ligeramente más clara que el fondo
//    tertiaryContainer = Color(0xFF1C1C1C) // Gris oscuro en lugar de blanco
//)
private val DarkColorScheme = lightColorScheme(
    primary =  Color(0xFF3343E0),
    secondary = Color.Yellow,
    secondaryContainer = Color.Black, //fondo de los iconos
    surfaceContainer = Color.Red,
    primaryContainer = Color.Yellow,
    onSurfaceVariant = Color.Black, //iconos sin seleccionar y texto de dropdown unfocused
    background =Color(0xFFF5F6FD),
    surface = Color(0xFFFFFBFE),
    tertiaryContainer = Color.White,
)
private val LightColorScheme = lightColorScheme(
    primary =  Color(0xFF3343E0),
    secondary = Color.Yellow,
    secondaryContainer = Color.Black, //fondo de los iconos
    surfaceContainer = Color.Red,
    primaryContainer = Color.Yellow,
    onSurfaceVariant = Color.Black, //iconos sin seleccionar y texto de dropdown unfocused
    background =Color(0xFFF5F6FD),
    surface = Color(0xFFFFFBFE),
    tertiaryContainer = Color.White,
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