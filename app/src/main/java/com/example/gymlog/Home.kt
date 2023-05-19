package com.example.gymlog

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {

    AppScreen(content = {
        AndroidView(factory = { context ->
            LayoutInflater.from(context).inflate(R.layout.home, null)
        },
            update = { view ->

            })
    }, navController = navController)
}

@Composable
fun workOutBtn(){

}