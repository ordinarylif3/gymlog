package com.example.gymlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.gymlog.ui.theme.GymLogTheme


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymLogTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }

    }
}

