package com.example.gymlog

import WorkoutViewModel
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip

@Composable
fun WorkoutDetail(navController: NavController, sessionID: String) {
    val fieldDataListState = remember { mutableStateOf<List<DocumentData>>(emptyList()) }

    //Log.d("Debug", sessionID)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.grey)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        AppScreen(navController = navController)
        DisplayDetails(sessionID, fieldDataListState)

    }

}

suspend fun fetchSessionID(Id: String): List<DocumentData> {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val documents = mutableListOf<DocumentData>()

    try {
        val collectionRef = db.collection("users")
            .document(auth.uid.toString())
            .collection("workoutSession")
            .document(Id)
            .collection("WorkoutCollection")
       // Log.d("Debug", collectionRef.toString())
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
fun DisplayDetails(sessionId: String, fieldDataListState: MutableState<List<DocumentData>>) {

    LaunchedEffect(sessionId) {
        val fieldDataList = fetchSessionID(sessionId)
        fieldDataListState.value = fieldDataList
    }

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

        LazyColumn(
            modifier = Modifier
                .padding(top = 5.dp)
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.gold),
                    shape = RoundedCornerShape(10.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //query for all workout data
            items(fieldDataListState.value) { fieldData ->
                Column(
                    modifier = Modifier
                        .padding(top = 2.dp, bottom = 2.dp)
                        .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
                        .border(
                            width = 1.dp,
                            color = colorResource(id = R.color.gold),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .width(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Exercise: ${fieldData.exerciseName}")
                    Text(text = "Reps: ${fieldData.repValue}")
                    Text(text = "Sets: ${fieldData.setValue}")
                    Text(text = "Weight: ${fieldData.weightValue}")
                    Spacer(modifier = Modifier.height(15.dp))
                }

            }

        }
    }

}
