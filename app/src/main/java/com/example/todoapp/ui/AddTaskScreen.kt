package com.example.todoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R

@Composable
fun AddTaskScreen(navigateUp: () -> Unit) {
    val taskTitle by rememberSaveable { mutableStateOf("") }
    val taskDate by rememberSaveable { mutableStateOf("") }
    val taskTime by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            ToDoAppBar(
                currentScreen = Screens.AddTask,
                isAddTaskScreen = true,
                navigateUp = { navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            BasicText(text = "What is to be done?")
            BasicTextField(input = taskTitle)
            Spacer(Modifier.height(20.dp))
            BasicText(text = "Due date")
            Row {
                BasicTextField(input = taskDate)
                Icon(painter = painterResource(id = R.drawable.ic_date), contentDescription = "date picker")
            }
            BasicTextField(input = taskTime)
        }
    }
}

@Composable
fun BasicText(text: String) {
    Text(text = text)
}

@Composable
fun BasicTextField(input: String) {
    BasicTextField(value = input, onValueChange = {  })
}