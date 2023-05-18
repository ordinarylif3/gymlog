package com.example.gymlog

import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun SignUp(){

    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.signup, null)
            val fullNameInput = view.findViewById<EditText>(R.id.create_fullname)
            val emailInput = view.findViewById<EditText>(R.id.create_email)
            val passInput = view.findViewById<EditText>(R.id.create_password)
            val confirmInput = view.findViewById<EditText>(R.id.create_confirm)

            val signupBtn = view.findViewById<Button>(R.id.signupbtn)
            signupBtn.setOnClickListener {
                Log.d("Debug", "button press")
                val name = fullNameInput.text.toString()
                val email = emailInput.text.toString()
                val password = passInput.text.toString()
                val confirmPass = confirmInput.text.toString()

                // Perform signup logic with firebase here using the email and password values

            }

            view
        },
        update = { view ->
            // Update the view here, if needed
        }
    )

}