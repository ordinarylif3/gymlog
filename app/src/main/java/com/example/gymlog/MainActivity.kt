package com.example.gymlog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.gymlog.ui.theme.GymLogTheme



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = rememberNavController()
        navController.setGraph(R.navigation.nav_graph)
        setContent {
            GymLogTheme {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { LoginScreen(navController = navController) }
                    // Add other destinations/screens as needed
                }
            }
        }
    }
}



@Preview
@Composable
fun LoginPreview() {
    GymLogTheme {
        LoginScreen()
    }
}

