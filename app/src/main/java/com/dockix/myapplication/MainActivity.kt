package com.dockix.myapplication

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dockix.myapplication.data.preferences.AppLanguage
import com.dockix.myapplication.data.preferences.ThemeMode
import com.dockix.myapplication.ui.navigation.NavGraph
import com.dockix.myapplication.ui.navigation.Screen
import com.dockix.myapplication.ui.theme.MyApplicationTheme
import com.dockix.myapplication.ui.viewmodel.SettingsViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val themeMode by settingsViewModel.themeMode.collectAsState()
            val appLanguage by settingsViewModel.appLanguage.collectAsState()
            
            // Apply language
            val context = LocalContext.current
            LaunchedEffect(appLanguage) {
                updateLanguage(context, appLanguage)
            }
            
            MyApplicationTheme(
                darkTheme = when (themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }
            ) {
                BudgetAppContent()
            }
        }
    }
    
    private fun updateLanguage(context: Context, appLanguage: AppLanguage) {
        val locale = when (appLanguage) {
            AppLanguage.ENGLISH -> Locale("en")
            AppLanguage.RUSSIAN -> Locale("ru")
        }
        
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}

data class BottomNavItem(
    val route: String,
    val titleResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    // Проверяем, находимся ли мы на SplashScreen
    val currentRoute = currentDestination?.route
    val showBottomBar = currentRoute != Screen.Splash.route
    
    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                route = Screen.Home.route,
                titleResId = R.string.nav_home,
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            ),
            BottomNavItem(
                route = Screen.Budget.route,
                titleResId = R.string.nav_budget,
                selectedIcon = Icons.Filled.Star,
                unselectedIcon = Icons.Outlined.Star
            ),
            BottomNavItem(
                route = Screen.Transactions.route,
                titleResId = R.string.nav_transactions,
                selectedIcon = Icons.Filled.List,
                unselectedIcon = Icons.Outlined.List
            ),
            BottomNavItem(
                route = Screen.Settings.route,
                titleResId = R.string.nav_settings,
                selectedIcon = Icons.Filled.Settings,
                unselectedIcon = Icons.Outlined.Settings
            )
        )
    }
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = stringResource(id = item.titleResId)
                                )
                            },
                            label = { Text(stringResource(id = item.titleResId)) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavGraph(navController = navController)
        }
    }
}