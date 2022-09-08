package com.example.todoapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.R

enum class Screens() {
    ListTasks,
    AddTask

//    object ListScreen : Screens(title = R.string.list_screen_title)
//    object AddTask : Screens(title = R.string.new_task_title)
}

@Composable
fun ToDoAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = currentScreen) },
        backgroundColor = colorResource(id = R.color.purple_200),
        navigationIcon = {
            if (canNavigateBack) {
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
fun ToDoApp(
    navController: NavHostController = rememberNavController()
) {

    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(topBar = {
        ToDoAppBar(
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() },
            currentScreen = backStackEntry?.destination?.route ?: Screens.ListTasks.name,
        )
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.ListTasks.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screens.ListTasks.name
            ) {
                ListScreen {
                    navController.navigate(Screens.AddTask.name)
                }
            }
            composable(route = Screens.AddTask.name) {
                AddTaskScreen()
            }
        }
    }
}

