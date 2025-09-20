package com.example.memoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.memoapp.ui.screens.AddEditMemoScreen
import com.example.memoapp.ui.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(navController = navController)
        }
        
        composable(NavRoutes.ADD_MEMO) {
            AddEditMemoScreen(navController = navController, memoId = null)
        }
        
        composable(NavRoutes.EDIT_MEMO) { backStackEntry ->
            val memoId = backStackEntry.arguments?.getString("memoId")?.toIntOrNull()
            AddEditMemoScreen(navController = navController, memoId = memoId)
        }
    }
}
