package com.example.gymlog

object WorkoutModelSingleton {
    var workoutModel: WorkoutModel? = null

    fun initializeWorkoutModel(sessionName: String, dateTime: Pair<String, String>, sessionID: String) {
        workoutModel = WorkoutModel(sessionName, dateTime, sessionID)
    }
}
