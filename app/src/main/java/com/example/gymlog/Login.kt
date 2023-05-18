package com.example.gymlog

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

@Composable
fun LoginScreen(navController: NavController){
    val navController = rememberNavController()

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
                navController.navigate(R.id.signupFragment)
                // Perform login logic with firebase here using the email and password values

            }

            view
        },
        update = { view ->
            // Update the view here, if needed
        }
    )

}



