package com.dockix.myapplication.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dockix.myapplication.R
import com.dockix.myapplication.data.preferences.AppLanguage
import com.dockix.myapplication.data.preferences.ThemeMode

@Composable
fun SettingsScreen(
    currentTheme: ThemeMode,
    currentLanguage: AppLanguage,
    onThemeChanged: (ThemeMode) -> Unit,
    onLanguageChanged: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = stringResource(id = R.string.nav_settings),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Theme Settings
        SettingsSection(
            title = stringResource(id = R.string.theme_settings),
            icon = if (currentTheme == ThemeMode.DARK) Icons.Default.Brightness2 else Icons.Default.BrightnessHigh
        ) {
            ThemeOption(
                text = stringResource(id = R.string.system_theme),
                selected = currentTheme == ThemeMode.SYSTEM,
                onClick = { onThemeChanged(ThemeMode.SYSTEM) }
            )
            
            ThemeOption(
                text = stringResource(id = R.string.light_theme),
                selected = currentTheme == ThemeMode.LIGHT,
                onClick = { onThemeChanged(ThemeMode.LIGHT) }
            )
            
            ThemeOption(
                text = stringResource(id = R.string.dark_theme),
                selected = currentTheme == ThemeMode.DARK,
                onClick = { onThemeChanged(ThemeMode.DARK) }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Language Settings
        SettingsSection(
            title = stringResource(id = R.string.language_settings),
            icon = Icons.Default.Language
        ) {
            LanguageOption(
                text = stringResource(id = R.string.language_english),
                selected = currentLanguage == AppLanguage.ENGLISH,
                onClick = { onLanguageChanged(AppLanguage.ENGLISH) }
            )
            
            LanguageOption(
                text = stringResource(id = R.string.language_russian),
                selected = currentLanguage == AppLanguage.RUSSIAN,
                onClick = { onLanguageChanged(AppLanguage.RUSSIAN) }
            )
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

@Composable
fun ThemeOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        
        if (selected) {
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun LanguageOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
        
        if (selected) {
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
} 