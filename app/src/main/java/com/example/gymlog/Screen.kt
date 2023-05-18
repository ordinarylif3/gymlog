package com.example.gymlog

sealed class Screen(val route : String){
    object Login: Screen (route = "loginScreen")
    object SignUp: Screen (route = "signupScreen")
}
