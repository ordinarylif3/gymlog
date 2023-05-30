package com.example.gymlog

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gymlog.WorkoutModelSingleton.workoutModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@Composable
fun HomeScreen(navController: NavController) {

    var showDialog by remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AppScreen(navController = navController)
        FeedBox()

        WorkoutButton(onClick = { showDialog = true })

        WorkoutPopup(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            navController = navController
        )
    }
}

@Composable
fun FeedBox() {
    Box(
        modifier = Modifier
            .size(width = 360.dp, height = 650.dp)
            .padding(top = 20.dp, bottom = 5.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 2.dp,
                color = colorResource(id = R.color.gold),
                shape = RoundedCornerShape(16.dp)
            )
            .background(colorResource(R.color.lightGrey)),
        contentAlignment = Alignment.Center,
    ) {
        // Content of the box, this is where I will query the data from firebase
        // and add the fist bump
    }
}
@Composable
fun WorkoutButton(onClick: ()-> Unit){
    Button(modifier = Modifier
        .size(height = 70.dp, width = 200.dp)
        .padding(top = 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.gold)),
        onClick = onClick){
        Text(text = "Start workout")
    }
}

fun getCurrentDateTime(): Pair<String, String> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    val timeFormat = SimpleDateFormat("HH:mm")
    val currentDate = Date()
    val date = dateFormat.format(currentDate)
    val time = timeFormat.format(currentDate)
    return Pair(date, time)
}

@Composable
fun SubmitButton(onClick: ()-> Unit){
    Button(modifier = Modifier,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        onClick = onClick){
        Text(text = "Submit")
    }
}

@Composable
fun WorkoutPopup(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    navController: NavController
) {

    val textFieldValue = remember { mutableStateOf("") }

    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .size(height = 200.dp, width = 400.dp)
                    .background(colorResource(id = R.color.gold))
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp),

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextInputField(
                        value = textFieldValue.value,
                        onValueChange = { newValue ->
                            textFieldValue.value = newValue
                        }
                    )

                    SubmitButton(onClick = {
                        //var workout = createWorkoutSession(textFieldValue.value)
                        // Create a new workout document for the user
                        //FirestoreRepository.addWorkoutSession(textFieldValue.value, getCurrentDateTime()) // Handle failure
                        //FirestoreRepository.addWorkoutSession(workout) // Handle failure
                        //Log.d("Debug", "submit button press")
                        //val dateTimeInfo = Pair(workoutModel?.dataTime?.first.toString(),
                          //  workoutModel?.dataTime?.second.toString())
                        // Workout name added successfully
                        // initalize singleton class here to get sessionID
                        WorkoutModelSingleton.initializeWorkoutModel(
                            textFieldValue.value,
                            getCurrentDateTime(),
                            generateSessionId()
                            )

                        workoutModel?.let { FirestoreRepository.addWorkoutSession(it) }


                        navController.navigate(route = Screen.Tracking.route)
                    })

                }
            }
        }
    }
}

fun generateSessionId(): String {
    return UUID.randomUUID().toString()
}
fun createWorkoutSession(session: String): WorkoutModel {
    val dateTime = getCurrentDateTime()
    // Generate a unique session ID
    return WorkoutModel(session, dateTime)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 20.dp),
        placeholder = { Text("Enter the workout session name.") }
    )
}


@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen(navController = rememberNavController())
}


