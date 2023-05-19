package com.example.gymlog

import android.content.Intent
import android.content.pm.ModuleInfo
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

@Composable
fun ProfileScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val imageUriState = remember { mutableStateOf<Uri?>(null) }

    val usersRef = FirebaseDatabase.getInstance().reference.child("users")

    AppScreen(content = {
        Box() {

            AndroidView(factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.profile, null)

                val signout = view.findViewById<TextView>(R.id.signOutBtn)
                signout.setOnClickListener {
                    auth.signOut()
                    navController.navigate(route = Screen.Login.route)
                }
                //val saveBtn = view.findViewById<EditText>(R.id.saveProfileBtn)

//                saveBtn.setOnClickListener {
//                    if(name.toString().isNotEmpty() && email.toString().isNotEmpty()){
//
//                    }
//                }

                view

            },
                update = { view ->

                    val fullname = view.findViewById<TextView>(R.id.usernamePlaceHolder)
                    val currUser = auth.currentUser
                    val query: Query = usersRef.orderByChild("email").equalTo(currUser?.email)

                    query.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (snapshot in dataSnapshot.children) {
                                val user: Users? = snapshot.getValue(Users::class.java)
                                fullname.text = user?.fullname

                                // Process the retrieved name
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle error
                        }
                    })

                }
            )
            CenterButton(onImageSelected = { imageUri ->
                imageUriState.value = imageUri
            })
        }
    }, navController = navController)



}

@Composable
fun CenterButton(onImageSelected: (Uri) -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularButtonWithText(onImageSelected = onImageSelected)
    }
}
@Composable
fun CircularButtonWithText(
    onImageSelected: (Uri) -> Unit
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onImageSelected(it) }
        }
    )

//    fun launchImagePicker(launcher: ActivityResultLauncher<Intent>) {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        launcher.launch(intent)
//    }

    Box(
        modifier = Modifier
            .size(150.dp)
            .background(Color.White, shape = CircleShape)
            .clickable { launcher.launch("image/*") },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Add Image",
            color = Color.Black,
            style = TextStyle(fontSize = 16.sp)
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview(){
    ProfileScreen(navController = rememberNavController())
}


