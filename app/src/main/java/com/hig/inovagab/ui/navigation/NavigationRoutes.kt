package com.hig.inovagab.ui.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hig.inovagab.model.UserRole
import com.hig.inovagab.ui.screens.HomeScreen
import com.hig.inovagab.ui.screens.IdeaScreen
import com.hig.inovagab.ui.screens.LoginScreen

@Composable
fun NavigationRoutes(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(onLoginSuccess = { role -> navController.navigate("home/$role"){popUpTo("login"){inclusive = true}} })

        }
        composable("home/{role}"){ backStackEntry -> val roleArg = backStackEntry.arguments?.getString("role") ?: "OPERATOR"
            val role = UserRole.entries.find { it.name == roleArg } ?: UserRole.OPERATOR
            HomeScreen(role = role,
                onLogout = { navController.navigate("login") {popUpTo(navController.graph.id) { inclusive = true } } },
                onNavigateToIdeas = { navController.navigate("ideas/${role.name}") })
        }
        composable("ideas/{role}") { backStackEntry ->
            val roleString = backStackEntry.arguments?.getString("role") ?: UserRole.OPERATOR.name
            val role = UserRole.valueOf(roleString)
            val userId = "user123" //Provisório

            IdeaScreen(
                role = role,
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }



    }

}