package com.dockix.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dockix.myapplication.ui.screens.BudgetScreen
import com.dockix.myapplication.ui.screens.HomeScreen
import com.dockix.myapplication.ui.screens.SettingsScreen
import com.dockix.myapplication.ui.screens.SplashScreen
import com.dockix.myapplication.ui.screens.TransactionsScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Budget : Screen("budget")
    data object Transactions : Screen("transactions")
    data object Settings : Screen("settings")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen()
        }
        
        composable(Screen.Budget.route) {
            BudgetScreen()
        }
        
        composable(Screen.Transactions.route) {
            TransactionsScreen()
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
} 