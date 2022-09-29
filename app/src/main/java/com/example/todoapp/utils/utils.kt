package com.example.todoapp.utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.cancellation.CancellationException

fun CollectionReference.snapshotListener(): Flow<QuerySnapshot> = callbackFlow {
    val listener = this@snapshotListener.addSnapshotListener { value, error ->
        if (error != null) {
            cancel(CancellationException(error))
        }
        if (value != null) {
            trySend(value)
        }
    }
        awaitClose {
            listener.remove()
        }
    }
