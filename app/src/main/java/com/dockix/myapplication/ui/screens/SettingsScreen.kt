package com.dockix.myapplication.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dockix.myapplication.ui.components.SettingsScreen as SettingsScreenComponent
import com.dockix.myapplication.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val themeMode by settingsViewModel.themeMode.collectAsState()
    val appLanguage by settingsViewModel.appLanguage.collectAsState()
    
    SettingsScreenComponent(
        currentTheme = themeMode,
        currentLanguage = appLanguage,
        onThemeChanged = { settingsViewModel.setThemeMode(it) },
        onLanguageChanged = { settingsViewModel.setAppLanguage(it) },
        modifier = Modifier.fillMaxSize()
    )
} 