package com.example.al_raqi_university_hospital_qms.ui.components

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.core.os.ConfigurationCompat

@Composable
fun LanguageSwitcher(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("hospital_qms_prefs", Context.MODE_PRIVATE)

    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]?.language ?: "en"

    val displayLanguage = if (currentLocale == "ar") "العربية" else "English"

    Row(
        modifier = modifier
            .clickable {
                val newLocale = if (currentLocale == "ar") "en" else "ar"
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(newLocale)
                AppCompatDelegate.setApplicationLocales(appLocale)

                // Save the new locale preference
                sharedPreferences.edit().putString("app_locale", newLocale).apply()
            }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = "Language",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = displayLanguage,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
