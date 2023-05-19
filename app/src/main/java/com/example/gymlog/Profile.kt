package com.example.gymlog

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun ProfileScreen(navController: NavController) {

    AppScreen(content = {
        AndroidView(factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.profile, null)
            view

        },
            update = {view ->

            }
        )
    }, navController = navController)


}