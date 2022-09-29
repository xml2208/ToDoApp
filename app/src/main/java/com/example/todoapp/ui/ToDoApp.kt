package com.example.todoapp.ui

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.R
import com.example.todoapp.data.TaskRepo

enum class Screens {
    ListTasks,
    AddTask
}

@Composable
fun ToDoAppBar(
    currentScreen: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = currentScreen) },
        backgroundColor = colorResource(id = R.color.app_bar_color),
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

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun ToDoApp(
    navController: NavHostController = rememberNavController()
) {
    val taskRepo by lazy { TaskRepo.get() }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val ctx = LocalContext.current
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
                AddTaskScreen(onSaveClicked = { task ->
                    taskRepo.taskCollection.add(task)
                    navController.navigate(Screens.ListTasks.name) {
                            popUpTo(Screens.AddTask.name) {
                                inclusive = true
                        }
                    }
                    Toast.makeText(ctx, task.time, Toast.LENGTH_SHORT).show()
                    setAlarmWithWorker(task.title, ctx)
                })
            }
        }
    }
}


