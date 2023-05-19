package com.example.gymlog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


@Composable
fun SignUp(navController: NavController){

    val auth = FirebaseAuth.getInstance()
    val usersRef = FirebaseDatabase.getInstance().reference.child("users")

    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context).inflate(R.layout.signup, null)
            val fullname = view.findViewById<EditText>(R.id.create_fullname)
            val emailInput = view.findViewById<EditText>(R.id.create_email)
            val passInput = view.findViewById<EditText>(R.id.create_password)
            val confirmInput = view.findViewById<EditText>(R.id.create_confirm)

            val backArrow = view.findViewById<ImageView>(R.id.backArrow)
            backArrow.setOnClickListener {
                navController.navigate(route = Screen.Login.route)
            }

            val createBtn = view.findViewById<Button>(R.id.createBtn)
            createBtn.setOnClickListener {
                Log.d("Debug", "button press")
                val name = fullname.text.toString()
                val email = emailInput.text.toString()
                val password = passInput.text.toString()
                val confirmPass = confirmInput.text.toString()

                // Perform signup logic with firebase here using the email and password values
                if(password == confirmPass) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // User creation successful
                                val user = auth.currentUser
                                Toast.makeText(context, "You have created your account!", Toast.LENGTH_SHORT).show()
                                navController.navigate(route = Screen.Home.route)
                                val newUserRef = usersRef.push()
                                val newUser = Users(name, email)
                                newUserRef.setValue(newUser)
                            } else {
                                // User creation failed
                                val exception = task.exception
                                Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else if (email.isEmpty() || password.isEmpty() || confirmPass.isEmpty()){
                    Toast.makeText(context, "Please enter all fields.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, "Passwords does not match.", Toast.LENGTH_SHORT).show()

                }
            }


            view
        },
        update = { view ->
            // Update the view here, if needed
        }
    )

    //Database Reference
// Read data from a specific node

//    usersRef.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            // Handle the data snapshot
//            for (userSnapshot in snapshot.children) {
//                val user = userSnapshot.getValue(User::class.java)
//                // Process the user data
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            // Handle the error
//        }
//    })
//
//// Write data to a specific node
//    val newUserRef = usersRef.push()
//    val newUser = User("John", "Doe")
//    newUserRef.setValue(newUser)
// Write data to a specific node
//    val newUserRef = usersRef.push()
//    val newUser = User("John", "Doe")
//    newUserRef.setValue(newUser)

}
