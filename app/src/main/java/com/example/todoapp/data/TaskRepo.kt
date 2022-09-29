package com.example.todoapp.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.cancellation.CancellationException

class TaskRepo(private val context: Context) {
    val taskCollection = FirebaseFirestore.getInstance().collection("Task")

    fun getTasks() = callbackFlow {
        val snapshotListener = taskCollection.addSnapshotListener { value, error ->
           if (error != null) {
                cancel(CancellationException(error))
            }
            if (value != null) {
                trySend(value)
            }
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var INSTANCE: TaskRepo? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepo(context)
            }
        }
        fun get(): TaskRepo {
            return INSTANCE ?: throw IllegalStateException("TaskRepo must be initialized")
        }
    }
}