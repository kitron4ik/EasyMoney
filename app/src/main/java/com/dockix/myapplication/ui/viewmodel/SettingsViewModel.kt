package com.dockix.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dockix.myapplication.data.preferences.AppLanguage
import com.dockix.myapplication.data.preferences.ThemeMode
import com.dockix.myapplication.data.preferences.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val userPreferences = UserPreferences(application)
    
    val themeMode: StateFlow<ThemeMode> = userPreferences.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeMode.SYSTEM
        )
    
    val appLanguage: StateFlow<AppLanguage> = userPreferences.appLanguage
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppLanguage.ENGLISH
        )
    
    fun setThemeMode(themeMode: ThemeMode) {
        viewModelScope.launch {
            userPreferences.setThemeMode(themeMode)
        }
    }
    
    fun setAppLanguage(language: AppLanguage) {
        viewModelScope.launch {
            userPreferences.setAppLanguage(language)
        }
    }
} 