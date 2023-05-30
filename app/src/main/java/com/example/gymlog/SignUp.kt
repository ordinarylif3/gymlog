package com.example.gymlog

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController){

    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.grey))
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                //backArrow(navController)
                Image(
                    painter = painterResource(id = R.drawable.cornerpiece),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                backArrow(navController)
            }
            Text(
                text = "Create Account",
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.century)),
                color = Color.White,
                modifier = Modifier.padding(top = 20.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { value -> name = value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                label = { Text("Full Name") },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { value -> email = value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                label = { Text("Email") },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { value -> password = value },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                label = { Text("Password") },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White),
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = confirmPass,
                onValueChange = { value -> confirmPass = value},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                label = { Text("Confirm Password") },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = Color.White),
                visualTransformation = PasswordVisualTransformation()
            )

                Button( colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.gold)),
                    onClick = {
                        if(password == confirmPass) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // User creation successful
                                val user = auth.currentUser
                                navController.navigate(route = Screen.Home.route)

                                // Get the Firestore instance
                                val db = FirebaseFirestore.getInstance()

                                // Create a new user document
                                val userData = hashMapOf(
                                    "name" to name,
                                    "email" to email
                                )

                                // Set the user document in the "users" collection
                                val userDocument = user?.let { it ->
                                    db.collection("users").document(
                                        it.uid)
                                }
                                if (userDocument != null) {
                                    userDocument.set(userData)
                                        .addOnSuccessListener {
                                            // User document added successfully
                                        }
                                        .addOnFailureListener { e ->
                                            // Error adding user document
                                        }
                                }

                            } else {
                                // User creation failed
                                val exception = task.exception
                                Log.d("Debug", exception.toString())
                            }
                        }
                }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    //colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Create Account",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        //style = MaterialTheme.typography.button
                    )
                }

        }

}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUp(navController = rememberNavController())
}

@Composable
fun backArrow(navController: NavController) {
    Image(
        painter = painterResource(id = R.drawable.baseline_arrow_back_24), // Replace with your drawable resource
        contentDescription = null,
        modifier = Modifier
            .size(30.dp)
            .clickable {
            navController.navigate(Screen.Login.route)
        }
    )
}