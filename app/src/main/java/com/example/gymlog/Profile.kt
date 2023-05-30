package com.example.gymlog

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavController) {
    //val auth = FirebaseAuth.getInstance()

    val imageUriState = remember { mutableStateOf<Uri?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey))
    ) {
        Column(modifier = Modifier
            .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
            AppScreen(navController = navController)
            CenterButton(onImageSelected = { imageUri ->
                imageUriState.value = imageUri
                //val imageURL = imageUriState.value.toString()
            })

            GetFullName()

            

            
        }

    }
}

@Composable
fun AddFriendsButton() {

}

@Composable
fun ShowFriends() {

}

@Composable
fun GetFullName() {
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    val collectionRef = db.collection("users")
    val currUser = auth.currentUser

    val fullNameState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        collectionRef.whereEqualTo("email", currUser?.email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val name = document.getString("name")
                    // Update the state with the user's name
                    if (name != null) {
                        fullNameState.value = name
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle failed query
                // Log or display the error message
            }
    }

    // Display the name in a Text composable
    Text(modifier = Modifier
        .padding(top = 5.dp),
        text = fullNameState.value,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.gold)
        )
    )
}

@Composable
fun CenterButton(onImageSelected: (Uri) -> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
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


