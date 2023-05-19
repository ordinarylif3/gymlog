package com.example.gymlog

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController,
    startDestination = Screen.Login.route){
       composable(
           route = Screen.Login.route
       ){
            LoginScreen(navController = navController)
       }
        composable(route = Screen.SignUp.route) {
            SignUp(navController = navController)
        }

      composable(route = Screen.Home.route){
          HomeScreen(navController = navController)
      }
        
        composable(route = Screen.Profile.route){
            ProfileScreen(navController = navController)
        }

        composable(route = Screen.Feed.route){
            HomeScreen(navController = navController)
        }

        composable(route = Screen.Chart.route){
            HomeScreen(navController = navController)
        }

        composable(route = Screen.Bell.route){
            HomeScreen(navController = navController)
        }

    }
}