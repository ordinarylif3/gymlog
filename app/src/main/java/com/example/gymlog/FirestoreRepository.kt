package com.example.gymlog

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirestoreRepository {
    private val firestore = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val db = FirebaseFirestore.getInstance()
    // Reference to the parent document
    val parentDocumentRef = user?.let { db.collection("users").document(it.uid) }
    val workoutSession = parentDocumentRef?.collection("workoutSession")

//    fun addWorkoutSession(sessionName: String, dateTime: Pair<String, String>) {
//        val workoutData = hashMapOf(
//            "sessionName" to sessionName,
//            "dateTime" to dateTime,
//        )
//
//        workoutSession?.add(workoutData)
//            ?.addOnSuccessListener { documentReference ->
//                val sessionId = documentReference.id
//                //WorkoutModel.sessionID = sessionId
//                // Workout name added successfully
//            }
//            ?.addOnFailureListener { e ->
//                // Error adding workout name
//            }
//    }

    fun addWorkoutSession(workoutModel: WorkoutModel) {
        val workoutData = hashMapOf(
            "sessionName" to workoutModel.sessionName,
            "dateTime" to workoutModel.dataTime,
        )

        workoutModel.sessionID?.let {
            workoutSession?.document(it)
                ?.set(workoutData)
                ?.addOnSuccessListener { documentReference ->

                }
                ?.addOnFailureListener { e ->
                    // Error adding workout name
                }

        }
    }

    fun addWorkoutRecord(workoutModel: WorkoutModel, exerciseName: String, setValue: String, repValue: String, weightValue: String) {
        val workoutRecord = hashMapOf(
            "exerciseName" to exerciseName,
            "setValue" to setValue,
            "repValue" to repValue,
            "weightValue" to weightValue
        )

        workoutModel.sessionID?.let { sessionId ->
            val sessionDocumentRef = workoutSession?.document(sessionId)?.collection("WorkoutCollection")
            // Create a subcollection reference within the document
            //val newCollectionRef = sessionDocumentRef?.collection("WorkoutCollection")

            sessionDocumentRef
                //?.collection(exerciseName) // Replace "subcollectionName" with the desired name of the subcollection
                ?.add(workoutRecord)
                ?.addOnSuccessListener { documentReference ->
                    // Document added successfully to the subcollection
                }
                ?.addOnFailureListener { e ->
                    // Error adding document to the subcollection
                }

        }
    }




}
