package com.example.gymlog

sealed class Screen(val route : String){
    object Login: Screen (route = "loginScreen")
    object SignUp: Screen (route = "signupScreen")
    object Home: Screen (route = "HomeScreen")
    object Profile: Screen (route = "ProfileScreen")
    object Feed: Screen(route = "FeedScreen")
    object Chart: Screen(route = "ChartScreen")
    object Bell: Screen(route = "NotificationScreen")
}
