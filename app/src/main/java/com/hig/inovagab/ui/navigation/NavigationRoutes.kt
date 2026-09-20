package com.hig.inovagab.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hig.inovagab.data.local.SessionManager
import com.hig.inovagab.data.utils.UserRole
import com.hig.inovagab.ui.screens.HomeScreen
import com.hig.inovagab.ui.screens.IdeaScreen
import com.hig.inovagab.ui.screens.LoginScreen
import com.hig.inovagab.ui.screens.ProjectScreen
import com.hig.inovagab.ui.screens.StrategyScreen
import kotlinx.coroutines.launch

private fun NavBackStackEntry.roleArg(): UserRole =
    UserRole.entries.find { it.name == arguments?.getString("role") } ?: UserRole.OPERATOR

private fun NavBackStackEntry.userIdArg(): String =
    arguments?.getString("userId").orEmpty()

@Composable
fun NavigationRoutes() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context.applicationContext) }
    val scope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = { role, userId ->
                    navController.navigate("home/${role.name}/$userId") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home/{role}/{userId}") { backStackEntry ->
            val role = backStackEntry.roleArg()
            val userId = backStackEntry.userIdArg()
            HomeScreen(
                role = role,
                onLogout = {
                    scope.launch { sessionManager.clearSession() }
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onNavigateToIdeas = { navController.navigate("ideas/${role.name}/$userId") },
                onNavigateToProjects = { navController.navigate("projects/${role.name}/$userId") },
                onNavigateToStrategies = { navController.navigate("strategies/${role.name}/$userId") }
            )
        }

        composable("ideas/{role}/{userId}") { backStackEntry ->
            IdeaScreen(
                role = backStackEntry.roleArg(),
                userId = backStackEntry.userIdArg(),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("projects/{role}/{userId}") { backStackEntry ->
            ProjectScreen(
                role = backStackEntry.roleArg(),
                userId = backStackEntry.userIdArg(),
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("strategies/{role}/{userId}") { backStackEntry ->
            StrategyScreen(
                role = backStackEntry.roleArg(),
                userId = backStackEntry.userIdArg(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}