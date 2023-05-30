package com.example.gymlog

data class WorkoutModel(
    var sessionName: String? = null,
    var dataTime: Pair<String, String>? = null,
    var sessionID: String? = null
)
