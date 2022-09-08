package com.example.todoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier

@Composable
fun AddTaskScreen() {
    val taskTitle by rememberSaveable { mutableStateOf("") }
    val taskDate by rememberSaveable { mutableStateOf("") }
    val taskTime by rememberSaveable { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicText(text = "This screen is for adding a new task")
        }
    }
}

@Composable
fun BasicText(text: String) {
    Text(text = text)
}

@Composable
fun BasicTextField(input: String) {
    BasicTextField(value = input, onValueChange = { })
}