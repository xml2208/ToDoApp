package com.example.todoapp.model

import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.DocumentSnapshot

data class Task(val id :String = "", val title: String = "", val date: String = "", var time: String = "") {
    fun asMap(): Map<String, Any> = mapOf(
        "title" to title,
        "date" to date
    )

    companion object {
        val snapshotParser = SnapshotParser<Task> { snapshot: DocumentSnapshot ->
            Task(
                title = snapshot.get("title") as String,
                date = snapshot.get("date") as String,
                time = snapshot.get("time") as String
            )
        }
    }
}