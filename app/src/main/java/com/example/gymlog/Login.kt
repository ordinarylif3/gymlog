package com.example.gymlog

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlin.math.sign


@Composable
fun LoginScreen(navController: NavController){


    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.login, null)
            val emailInput = view.findViewById<EditText>(R.id.emailInput)
            val passwordInput = view.findViewById<EditText>(R.id.passwordInput)

            val loginBtn = view.findViewById<Button>(R.id.loginbtn)
            loginBtn.setOnClickListener {
                Log.d("Debug", "button press")
                val email = emailInput.text.toString()
                val password = passwordInput.text.toString()
                // Perform login logic with firebase here using the email and password values

            }

            val signupBtn = view.findViewById<Button>(R.id.signupbtn)
            signupBtn.setOnClickListener {
                navController.navigate(route = Screen.SignUp.route)
            }

            view
        },
        update = { view ->
            // Update the view here, if needed
        }
    )

}



