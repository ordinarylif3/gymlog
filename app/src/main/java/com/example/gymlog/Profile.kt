package com.example.gymlog

import android.content.pm.ModuleInfo
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.google.firebase.database.FirebaseDatabase

@Composable
fun ProfileScreen(navController: NavController) {

    val imageUriState = remember { mutableStateOf<Uri?>(null) }

    val usersRef = FirebaseDatabase.getInstance().reference.child("users")

    AppScreen(content = {
        Box() {

            AndroidView(factory = { context ->
                val view = LayoutInflater.from(context).inflate(R.layout.profile, null)
                val name = view.findViewById<EditText>(R.id.profile_full_name)
                val email = view.findViewById<EditText>(R.id.profile_email)
                val saveBtn = view.findViewById<EditText>(R.id.saveProfileBtn)

//                saveBtn.setOnClickListener {
//                    if(name.toString().isNotEmpty() && email.toString().isNotEmpty()){
//
//                    }
//                }

                view

            },
                update = { view ->
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


