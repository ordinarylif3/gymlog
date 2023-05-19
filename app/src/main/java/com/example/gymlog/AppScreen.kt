package com.example.gymlog

import android.view.LayoutInflater
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun AppScreen(
    content: @Composable () -> Unit,
    navController: NavController
) {
    AndroidView(factory = { context ->
        val view = LayoutInflater.from(context).inflate(R.layout.home, null)
        val home = view.findViewById<ImageView>(R.id.homeBtn)
        val profile = view.findViewById<ImageView>(R.id.profileBtn)
        val feed = view.findViewById<ImageView>(R.id.feedBtn)
        val chart = view.findViewById<ImageView>(R.id.chartBtn)
        val bell = view.findViewById<ImageView>(R.id.bellBtn)

        home.setOnClickListener {
            navController.navigate(route = Screen.Home.route)
        }

        profile.setOnClickListener {
            navController.navigate(route = Screen.Profile.route)
        }

        feed.setOnClickListener {
            navController.navigate(route = Screen.Feed.route)
        }

        chart.setOnClickListener {
            navController.navigate(route = Screen.Chart.route)
        }

        bell.setOnClickListener {
            navController.navigate(route = Screen.Bell.route)
        }

        view
    },
        update = {view ->

        }
    )
    // Content of the screen
    content()
}