package com.example.gymlog

import WorkoutViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gymlog.WorkoutModelSingleton.workoutModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

@Composable
fun HomeScreen(navController: NavController) {

    val workoutInfoList by remember { mutableStateOf(mutableStateOf<List<WorkoutSession>>(emptyList())) }

    var showDialog by remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AppScreen(navController = navController)
        FeedBox(workoutInfoList, navController)

        WorkoutButton(onClick = { showDialog = true })

        WorkoutPopup(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            navController = navController
        )
    }
}
@Composable
fun PostBox(workoutInfo: WorkoutSession, navController: NavController) {

    Column(
        modifier = Modifier
            .clickable {
                workoutInfo.sessionID?.let { sessionID ->
                    CoroutineScope(Dispatchers.Main).launch {
                        navController.navigate("${Screen.WorkoutDetail.route}/$sessionID")
                    }
                }
            }
            .height(80.dp)
            .fillMaxWidth()
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        workoutInfo.sessionName?.let { Text(text = it) }
        workoutInfo.dateTime?.let { dateTime ->
            Text(text = "Date: ${dateTime["first"]}")
            Text(text = "Time: ${dateTime["second"]}")
        }
    }
}


@Composable
fun FeedBox(workoutInfoList: MutableState<List<WorkoutSession>>, navController: NavController) {

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
        contentAlignment = Alignment.TopCenter,
    ) {

        LaunchedEffect(Unit) {
            val fetchedWorkoutInfoList = fetchAllSessions()
            workoutInfoList.value = fetchedWorkoutInfoList
        }

        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp)
                .width(340.dp)
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.gold),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(Color.Gray, shape = RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //query for all workout data
            items(workoutInfoList.value) { workoutInfo ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    PostBox(workoutInfo, navController)
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }

        }
    }
}

suspend fun fetchAllSessions(): List<WorkoutSession> {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val sessions = mutableListOf<WorkoutSession>()

    try {
        val collectionRef = db.collection("users")
            .document(auth.uid.toString())
            .collection("workoutSession")
            .orderBy("dateTime.first", Query.Direction.DESCENDING)

        val querySnapshot = collectionRef.get().await()

        for (documentSnapshot in querySnapshot.documents) {
            val session = documentSnapshot.toObject(WorkoutSession::class.java)
            session?.let {
                val documentId = documentSnapshot.id
                val sessionName = documentSnapshot.getString("sessionName")
                val dateTimeMap = documentSnapshot["dateTime"] as? Map<String, String>
                val workoutInfo = it.copy(
                    sessionID = documentId,
                    sessionName = sessionName,
                    dateTime = dateTimeMap
                )
                sessions.add(workoutInfo)
            }
        }
    } catch (e: Exception) {
        // Handle error
        Log.d("Debug", e.toString())
    }

    return sessions
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


