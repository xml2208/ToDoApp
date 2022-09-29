package com.example.todoapp.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.todoapp.R
import com.example.todoapp.data.NotificationWorker
import com.example.todoapp.model.Task
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

val calendar: Calendar = Calendar.getInstance()
val year = calendar[Calendar.YEAR]
val month = calendar[Calendar.MONTH]
val dayOfTheMonth = calendar[Calendar.DAY_OF_MONTH]
val hourOfTheDay = calendar[Calendar.HOUR_OF_DAY]
val minuteOfTheHour = calendar[Calendar.MINUTE]
val months =
    arrayOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
val todayInSeconds = calendar.apply { set(year, month, dayOfTheMonth, hourOfTheDay, minuteOfTheHour) }
var selectedTimeInSeconds by Delegates.notNull<Calendar>()

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddTaskScreen(onSaveClicked: (Task) -> Unit) {

    var taskTitle by rememberSaveable { mutableStateOf("") }
    var taskDate by rememberSaveable { mutableStateOf("") }
    var taskMonth by rememberSaveable { mutableStateOf(0) }
    var taskYear by rememberSaveable { mutableStateOf(0) }
    var taskDay by rememberSaveable { mutableStateOf(0) }
    var taskTime by rememberSaveable { mutableStateOf("") }
    var taskHour by rememberSaveable { mutableStateOf(0) }
    var taskMinute by rememberSaveable { mutableStateOf(0) }

    selectedTimeInSeconds = Calendar.getInstance().apply { set(taskYear, taskMonth, taskDay, taskHour, taskMinute) }

    val ctx = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onSaveClicked(
                    Task(
                        title = taskTitle,
                        date = taskDate,
                        time = taskTime
                    ),
                )
            }, backgroundColor = colorResource(id = R.color.app_bar_color)) {
                Icon(Icons.Default.Check, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
            ) {
                Text(
                    text = "What is to be done?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(5.dp)
                )
                BasicTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                )
                Divider()
                Text(
                    text = "Due date:", fontWeight = FontWeight.Bold,
                    fontSize = 18.sp, modifier = Modifier.padding(top = 40.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            onTaskDatePickerClicked(
                                context = ctx,
                                setDateText = { selectedTaskDate -> taskDate = selectedTaskDate },
                                setDateToCalendar = { year, month, day ->
                                    taskYear = year
                                    taskMonth = month
                                    taskDay = day
                                })
                        }
                ) {
                    Text(text = taskDate, Modifier.weight(1f), fontSize = 20.sp)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_date),
                        contentDescription = null,
                        Modifier
                            .align(Alignment.Bottom)
                    )
                }
                Divider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .clickable {
                            onTimePickerDialogClicked(
                                ctx,
                                setTimeText = { selectedTaskTime -> taskTime = selectedTaskTime},
                                setTimeToCalendar = { hour, minute ->
                                    taskHour = hour
                                    taskMinute= minute}
                            )
                        }
                ) {
                    Text(text = taskTime, fontSize = 20.sp, modifier = Modifier.weight(2f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = null,
                    )
                }
                Divider()
                DatePicker(ctx)
            }
        }
    }
}

fun setAlarmWithWorker(message: String, ctx: Context) {
    val delayInSeconds = (selectedTimeInSeconds.timeInMillis / 1000L - todayInSeconds.timeInMillis / 1000L)
    Log.d("sec", "selectedTimeInSeconds: $selectedTimeInSeconds")
    Log.d("sec", "todayInSeconds: $todayInSeconds")
    Log.d("sec", "delayed: $delayInSeconds")
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayInSeconds, TimeUnit.SECONDS)
            .setInputData(
                workDataOf(
                    "title" to "Reminder",
                    "message" to message
                )
            )
            .build()
        WorkManager.getInstance(ctx).enqueue(workRequest)
}

fun onTaskDatePickerClicked(context: Context, setDateText: (String) -> Unit, setDateToCalendar:(Int, Int, Int) -> Unit) {
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            setDateText("${months[month]} $dayOfMonth, $year")
            setDateToCalendar(year, month, dayOfMonth)
        }, year, month, dayOfTheMonth
    )
    mDatePickerDialog.show()
}

fun onTimePickerDialogClicked(context: Context, setTimeText: (String) -> Unit, setTimeToCalendar: (Int, Int) -> Unit) {
    val timePickerDialog = TimePickerDialog(
        context, { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
            setTimeText(String.format("%02d:%02d", selectedHour, selectedMinute))
            setTimeToCalendar(selectedHour, selectedMinute)
        }, hourOfTheDay, minuteOfTheHour, false
    )
    timePickerDialog.show()
}

@Preview
@Composable
fun AddTaskScreenPreview() {

}