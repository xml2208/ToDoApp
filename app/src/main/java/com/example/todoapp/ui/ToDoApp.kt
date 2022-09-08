package com.example.todoapp.ui

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.example.todoapp.R

sealed class Screens(@StringRes val title: Int) {
    object ListScreen : Screens(title = R.string.list_screen_title)
    object AddTask : Screens(title = R.string.new_task_title)
}

@Composable
fun ToDoAppBar(
    currentScreen: Screens,
    isAddTaskScreen: Boolean,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.title)) },
        backgroundColor = colorResource(id = R.color.purple_200),
        navigationIcon = {
            if (isAddTaskScreen) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.up_button)
                    )
                }
            }
        },
        contentColor = Color.White,
    )
}

@Composable
fun ToDoApp() {
    var showListScreen by rememberSaveable { mutableStateOf(true) }

    if (showListScreen) {
        ListScreen(onPlusClicked = { showListScreen = false })
    } else {
        AddTaskScreen(navigateUp = { showListScreen = true })
    }
}
