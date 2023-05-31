package com.example.gymlog

import com.google.firebase.firestore.PropertyName

data class WorkoutSession(
    val sessionID: String? = null,
    val sessionName: String? = null,
    val dateTime: Map<String, String>? = null
)