package com.dockix.myapplication.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

enum class AppLanguage {
    ENGLISH, RUSSIAN
}

/**
 * User preferences manager using SharedPreferences
 */
class UserPreferences(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    // Preferences keys
    private val themeModeKey = "theme_mode"
    private val languageKey = "app_language"
    
    // StateFlows for reactive updates
    private val _themeMode = MutableStateFlow(loadThemeMode())
    val themeMode: Flow<ThemeMode> = _themeMode.asStateFlow()
    
    private val _appLanguage = MutableStateFlow(loadAppLanguage())
    val appLanguage: Flow<AppLanguage> = _appLanguage.asStateFlow()
    
    /**
     * Load theme mode from SharedPreferences
     */
    private fun loadThemeMode(): ThemeMode {
        val value = sharedPreferences.getInt(themeModeKey, THEME_MODE_SYSTEM)
        return when (value) {
            THEME_MODE_LIGHT -> ThemeMode.LIGHT
            THEME_MODE_DARK -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }
    
    /**
     * Load app language from SharedPreferences
     */
    private fun loadAppLanguage(): AppLanguage {
        val value = sharedPreferences.getString(languageKey, LANGUAGE_ENGLISH) ?: LANGUAGE_ENGLISH
        return when (value) {
            LANGUAGE_ENGLISH -> AppLanguage.ENGLISH
            LANGUAGE_RUSSIAN -> AppLanguage.RUSSIAN
            else -> AppLanguage.ENGLISH
        }
    }
    
    /**
     * Set theme mode
     */
    suspend fun setThemeMode(mode: ThemeMode) {
        val value = when (mode) {
            ThemeMode.LIGHT -> THEME_MODE_LIGHT
            ThemeMode.DARK -> THEME_MODE_DARK
            ThemeMode.SYSTEM -> THEME_MODE_SYSTEM
        }
        
        sharedPreferences.edit().putInt(themeModeKey, value).apply()
        _themeMode.value = mode
    }
    
    /**
     * Set app language
     */
    suspend fun setAppLanguage(language: AppLanguage) {
        val value = when (language) {
            AppLanguage.ENGLISH -> LANGUAGE_ENGLISH
            AppLanguage.RUSSIAN -> LANGUAGE_RUSSIAN
        }
        
        sharedPreferences.edit().putString(languageKey, value).apply()
        _appLanguage.value = language
    }
    
    companion object {
        // Constants
        private const val PREFS_NAME = "user_preferences"
        
        // Theme mode values
        private const val THEME_MODE_SYSTEM = 0
        private const val THEME_MODE_LIGHT = 1
        private const val THEME_MODE_DARK = 2
        
        // Language values
        private const val LANGUAGE_ENGLISH = "en"
        private const val LANGUAGE_RUSSIAN = "ru"
    }
} 