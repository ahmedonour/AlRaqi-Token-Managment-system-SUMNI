package com.example.al_raqi_university_hospital_qms.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color // Added import
import com.example.al_raqi_university_hospital_qms.ui.theme.* // Import all custom colors

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = PrimaryDark, // Using PrimaryDark for tertiary in dark theme
    background = TextDark, // A darker background for dark theme
    surface = TextDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = BgLight, // Light text on dark background
    onSurface = BgLight,
    error = Danger
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = PrimaryDark, // Using PrimaryDark for tertiary in light theme
    background = BgLight,
    surface = BgLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark,
    error = Danger
)

@Composable
fun ALRaqiUniversityHospitalQMSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to false to always use our custom scheme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Removed dynamic color logic as dynamicColor is false
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}