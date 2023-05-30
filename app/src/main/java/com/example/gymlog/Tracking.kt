package com.example.gymlog
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gymlog.FirestoreRepository.db
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Composable
fun TrackingScreen(navController: NavController) {

    val exerciseCollection = "exercises"
    val db = FirebaseFirestore.getInstance()

    var exerciseList: List<String> by remember { mutableStateOf(emptyList()) }


    //query for exercise name
    LaunchedEffect(Unit) {
        try {
            val names = fetchExerciseNames(exerciseCollection)
            exerciseList = names
        } catch (e: Exception) {
            // Handle the error case
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppScreen(navController = navController)
        AddWorkoutBox(exerciseList)

        CompleteWorkoutButton (onClick = {
            navController.navigate(route = Screen.Home.route)
        })
    }

}

suspend fun fetchExerciseNames(exerciseCollection: String): List<String> = suspendCoroutine { continuation ->
    db.collection(exerciseCollection)
        .get()
        .addOnSuccessListener { querySnapshot ->
            val exerciseNames = mutableListOf<String>()
            for (document in querySnapshot) {
                val exerciseName = document.getString("exercise")
                exerciseName?.let {
                    exerciseNames.add(it)
                }
            }
            continuation.resume(exerciseNames)
        }
        .addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }
}



@Composable
fun PlusButton(onClick: ()-> Unit) {
    Button(modifier = Modifier.padding(top = 20.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = colorResource(id = R.color.gold)),
        onClick = onClick){
        Text(color = Color.Black, text = "+", fontSize = 40.sp)
    }
}

@Composable
fun CompleteWorkoutButton(onClick: () -> Unit) {
    Button(modifier = Modifier,
        onClick = onClick){
        Text(text = "Complete Workout")
    }
}

@Composable
fun AddWorkoutBox(exerciseList: List<String>) {
    var showAddDialog by remember { mutableStateOf(false)}

    val workoutModel = WorkoutModelSingleton.workoutModel
    val fieldDataListState = remember { mutableStateOf<List<DocumentData>>(emptyList()) }

    //query for documentData
    LaunchedEffect(Unit) {
        try {
            val fieldDataList = fetchDocuments(workoutModel?.sessionID.toString())
            fieldDataListState.value = fieldDataList
        } catch (e: Exception) {
            // Handle any errors that occurred during the query
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
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
        Column(modifier = Modifier
            .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
            UserInfoDisplay(modifier = Modifier
                .padding(top = 20.dp, bottom = 10.dp))
            LazyColumn(modifier = Modifier
                .border(width = 1.dp, color = colorResource(id = R.color.gold))
                .background(Color.Gray),
                horizontalAlignment = Alignment.CenterHorizontally) {
                //query for all workout data
                items(fieldDataListState.value) { fieldData ->
                        Text(text = "Name: ${fieldData.name}")
                        Text(text = "Reps: ${fieldData.reps}")
                        Text(text = "Sets: ${fieldData.sets}")
                        Text(text = "Weight: ${fieldData.weight}")
                }

            }
            PlusButton(onClick = { showAddDialog = true})

            AddWorkoutPopup(
                showAddDialog = showAddDialog,
                onDismiss = { showAddDialog = false },
                exerciseList,
                navController = rememberNavController()
            )


        }
    }
}

//@Preview
//@Composable
//fun WorkoutPopupPreview() {
//    AddWorkoutPopup(showAddDialog = true, onDismiss = false) {
//    }
//}


@Composable
fun AddWorkoutPopup(
    showAddDialog: Boolean,
    onDismiss: () -> Unit,
    exerciseList: List<String>,
    navController: NavController
) {

    var exerciseValue by remember { mutableStateOf("Select Exercise") }
    var isDropdownVisible by remember { mutableStateOf(false) }
    var setValue by remember { mutableStateOf("") }
    var repValue by remember { mutableStateOf("") }
    var weightValue by remember { mutableStateOf("") }

    if ( showAddDialog ) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .size(height = 200.dp, width = 400.dp)
                    .background(colorResource(id = R.color.grey))
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.gold),
                        RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .padding(10.dp),
                contentAlignment = Alignment.Center
                ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ExerciseButton(label = exerciseValue, onClick =  {
                        isDropdownVisible = true
                        Log.d("Debug", "Box pressed")
                    })

                    if(isDropdownVisible){
                        DropdownMenu(
                            expanded = isDropdownVisible,
                            onDismissRequest = { isDropdownVisible = false },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(height = 300.dp, width = 200.dp),
                            offset = DpOffset(x = 50.dp, y = (-170).dp),

                        ) {
                            Log.d("Debug", exerciseList[0])
                            LazyColumn(modifier = Modifier
                                .border(width = 1.dp, color = colorResource(id = R.color.gold))
                                .size(height = 300.dp, width = 200.dp)
                                .background(Color.Gray),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(exerciseList.size) { index ->
                                    DropdownMenuItem(modifier = Modifier
                                        .background(Color.LightGray)
                                        .align(Alignment.CenterHorizontally),
                                        onClick = {
                                            exerciseValue = exerciseList[index]
                                            isDropdownVisible = false
                                        }
                                    , text = {
                                            Text(exerciseList[index], textAlign = TextAlign.Center)
                                    })
                                }
                            }

                        }

                    }

                    Row(modifier = Modifier
                        //.fillMaxWidth()
                        .size(width = 400.dp, height = 50.dp),
                        //.background(Color.White),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically)
                    {
                        LabeledTextField(label = "Sets", value = setValue) { value ->
                            setValue = value
                            // Handle the input value here
                        }
                        LabeledTextField(label = "Reps", value = repValue) { value ->
                            repValue = value
                            // Handle the input value here
                        }
                        LabeledTextField(label = "Weight", value = weightValue) { value ->
                            weightValue = value
                            // Handle the input value here
                        }

                    }

                    AddWorkoutButton (onClick = {
                        //Add firestore logic to add workout info to db
                        Log.d("Debug","Add Workout pressed")
                        val workoutModel = WorkoutModelSingleton.workoutModel
                        if (workoutModel != null) {
                            FirestoreRepository.addWorkoutRecord(workoutModel,exerciseValue, setValue, repValue, weightValue)
                        }
                        navController.navigate(route = Screen.Tracking.route)
                    })
               }
            }
        }
    }
}

@Composable
fun AddWorkoutButton(onClick: ()-> Unit){
    Button(modifier = Modifier
        .padding(top = 10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.gold)),
        onClick = onClick){
        Text(text = "Add Workout")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledTextField(label: String, value: String, onValueChange: (String) -> Unit) {

        TextField(
            modifier = Modifier
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
                .size(width = 80.dp, height = 60.dp),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.LightGray
            ),

            placeholder = {
                    Text(
                        text = label,
                        textAlign = TextAlign.Center,
                        fontSize = 10.sp,
                        modifier = Modifier.size(width = 80.dp, height = 60.dp)
                    )
            }
        )
}



@Composable
fun ExerciseButton(label: String, onClick: () -> Unit)  {
    Box(modifier = Modifier
        .padding(bottom = 20.dp)
        .background(Color.LightGray, shape = RoundedCornerShape(15.dp))
        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(15.dp))
        .size(width = 300.dp, height = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier
                .background(Color.LightGray, shape = RoundedCornerShape(15.dp))
                .fillMaxSize(), onClick = onClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),

        ) {
            Text(
                text = label, textAlign = TextAlign.Center, fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp)
            )

        }

        TriangleShape(
            color = colorResource(id = R.color.gold),
            alignment = Alignment.CenterStart,
            size = 30.dp,
            modifier = Modifier
                .size(10.dp)
                .padding(start = 100.dp),
        )
    }
}

@Composable
fun TriangleShape(
    color: Color,
    alignment: Alignment,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.size(size)) {
        drawPath(
            path = Path().apply {
                val width = size.toPx()
                val height = width / 2f
                moveTo(0f, 0f)
                lineTo(width, 0f)
                lineTo(width / 2f, height)
                close()
            },
            color = color
        )
    }
}



suspend fun fetchDocuments(sessionID: String): List<DocumentData> {
    val db = FirebaseFirestore.getInstance()
    val documents = mutableListOf<DocumentData>()

    try {
        val collectionRef = db.collection("users")
            .document(sessionID)
            .collection("workoutCollection")
        val querySnapshot = collectionRef.get().await()

        for (documentSnapshot in querySnapshot.documents) {
            val documentData = documentSnapshot.toObject(DocumentData::class.java)
            documentData?.let {
                documents.add(it)
            }
        }
    } catch (e: Exception) {
        // Handle error
    }

    return documents
}


@Composable
fun UpdateWorkOutPage(){
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    val workoutModel = WorkoutModelSingleton.workoutModel
    val fieldDataListState = remember { mutableStateOf<List<DocumentData>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val fieldDataList = fetchDocuments(workoutModel?.sessionID.toString())
            fieldDataListState.value = fieldDataList
        } catch (e: Exception) {
            // Handle any errors that occurred during the query
        }
    }

    // Use the fetched field data in your UI
    for (fieldData in fieldDataListState.value) {
        Text(text = fieldData.toString())
    }

}

@Composable
fun UserInfoDisplay(modifier: Modifier) {

    val (currentDate, currentTime) = remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val db = Firebase.firestore
    val userCollection = db.collection("users")
    val collectionRef = db.collection("users/${auth.uid}/workoutSession")
    val currUser = auth.currentUser

    val fullNameState = remember { mutableStateOf("") }
    val date = remember { mutableStateOf("") }
    val time = remember {
        mutableStateOf("")
    }
    var sessionName = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        userCollection.whereEqualTo("email", currUser?.email)
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



    LaunchedEffect(Unit){
        val workoutModel = WorkoutModelSingleton.workoutModel

        Log.d("Debug", workoutModel?.sessionName.toString())
        Log.d("Debug", workoutModel?.sessionID.toString())
        val workoutSessionRef = workoutModel?.sessionID?.let {
            collectionRef.document(it)
        }

        workoutSessionRef
            ?.get()
            ?.addOnSuccessListener { documentSnapshot ->
                val one = documentSnapshot.getString("sessionName")
                if (one != null) {
                    // Update the sessionName state or perform other actions
                    sessionName.value = one
                }

                val dateTime = documentSnapshot.get("dateTime") as? Map<*, *>
                val two = dateTime?.get("first") as? String
                val three = dateTime?.get("second") as? String

                if (two != null && three != null) {
                    // Update the date and time states or perform other actions
                     date.value = two
                     time.value = three
                }

                // Do something with the retrieved information
                // For example, update a state or display the information in a composable
            }

    }


        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .size(width = 330.dp, height = 100.dp)
                .background(colorResource(id = R.color.gold)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp),
                    text = fullNameState.value,
                    color = Color.Black
                )
                Text(
                    modifier = Modifier
                        .padding(start = 150.dp,top = 5.dp),
                    text = time.value,
                    textAlign = TextAlign.End
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp),
                text = sessionName.value,
                color = Color.White,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier
                    .padding(start = 10.dp, top = 5.dp),
                text = date.value,
                color = Color.Black
            )

        }



}

@Preview
@Composable
fun TrackingPreview() {
    TrackingScreen(navController = rememberNavController())
}