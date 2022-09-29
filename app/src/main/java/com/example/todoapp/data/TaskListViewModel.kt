package com.example.todoapp.data

import androidx.lifecycle.ViewModel

class TaskListViewModel(private val taskRepo: TaskRepo): ViewModel() {
    val flow = taskRepo.getTasks()
}