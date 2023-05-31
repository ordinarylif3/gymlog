package com.example.gymlog

import WorkoutViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    val workoutViewModel: WorkoutViewModel = viewModel()

    NavHost(navController = navController,
    startDestination = Screen.Login.route){
       composable(route = Screen.Login.route ){
            LoginScreen(navController = navController)
       }
        composable(route = Screen.SignUp.route) {
            SignUp(navController = navController)
        }

      composable(route = Screen.Home.route){
          HomeScreen(navController = navController)
      }

        composable(route = Screen.Tracking.route){
            TrackingScreen(navController = navController)
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

        composable(route = "${Screen.WorkoutDetail.route}/{sessionID}") { backStackEntry ->
            val sessionID = backStackEntry.arguments?.getString("sessionID") ?: ""
            WorkoutDetail(navController = navController, sessionID = sessionID)
        }

    }
}