package com.example.todoapp.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.R
import com.example.todoapp.data.TaskRepo
import com.example.todoapp.model.Task

@Composable
fun ListScreen(onPlusClicked: () -> Unit) {
    val taskRepo by lazy { TaskRepo.get() }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onPlusClicked()
            }, backgroundColor = colorResource(id = R.color.app_bar_color)) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TasksList()
            taskRepo.taskCollection.get().addOnSuccessListener {
                for (doc in it) {
                    Log.d("asd", "Tasks:" + "${it.toObjects(Task::class.java)}")
                    Log.d("asd", "Tasks: ${doc.id} => ${doc.data}")
                }
            }
        }
    }
}

@Composable
fun TasksList() {
    val ctx = LocalContext.current
    val taskRepo by lazy { TaskRepo.get() }
    val flowOfQuerySnapshot = taskRepo.getTasks()
    val list =
        flowOfQuerySnapshot.collectAsState(initial = null).value?.documents?.map { documentSnapshot ->
            val task = Task.snapshotParser.parseSnapshot(documentSnapshot)
            Task(id = documentSnapshot.id, title = task.title, date = task.date, time = task.time)
        } ?: emptyList()

    LazyColumn(contentPadding = PaddingValues(10.dp)) {
        items(items = list) { task ->
            TaskItem(task = task, onCheckedChange = { isChecked, id ->
                Toast.makeText(ctx, "$isChecked", Toast.LENGTH_SHORT).show()
                if (isChecked) {
                    taskRepo.taskCollection.document(id).delete()
                }
            })
        }
    }
}

@Composable
fun TaskItem(task: Task, onCheckedChange: (Boolean, String) -> Unit) {
    var checkedState by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), elevation = 3.dp
    ) {
        Row {
            Checkbox(checked = checkedState, onCheckedChange = {
                checkedState = it
                onCheckedChange(checkedState, task.id)
            })
            Column {
                Text(text = task.title, fontSize = 18.sp)
                Row(verticalAlignment = CenterVertically) {
                    Icon(painter = painterResource(id =  R.drawable.ic_alarm), contentDescription = "ic_alarm")
                    Text(text = task.date, fontSize = 16.sp, color = colorResource(R.color.app_bar_color),modifier = Modifier.align(CenterVertically))
                    Text(text = " at ${task.time}", fontSize = 16.sp, color = colorResource(R.color.app_bar_color),  modifier = Modifier.align(CenterVertically))
                }
            }
        }
    }
}

@Preview
@Composable
fun ListScreenPreview() {
    Text(text =" task.date", fontSize = 16.sp)
}


