package com.hig.inovagab.ui.navigation

import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hig.inovagab.ui.screens.LoginScreen

@Composable
fun NavigationRoutes(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(onLoginSuccess = { role -> navController.navigate("home/$role"){popUpTo("login"){inclusive = true}} })

        }
        composable("home/{role}"){ backStackEntry -> val role = backStackEntry.arguments?.getString("role") ?: "OPERATOR"}

    }

}