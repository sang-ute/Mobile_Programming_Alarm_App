package com.example.alarmapp

import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var timeDisplayTextView: TextView
    private lateinit var setAlarmButton: Button
    private lateinit var selectTimeButton: Button
    private lateinit var alarmMessageEditText: EditText

    private var selectedHour = 0
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        timeDisplayTextView = findViewById(R.id.timeDisplayTextView)
        setAlarmButton = findViewById(R.id.setAlarmButton)
        selectTimeButton = findViewById(R.id.selectTimeButton)
        alarmMessageEditText = findViewById(R.id.alarmMessageEditText) // New EditText for message input

        // Set default time to current time
        val calendar = Calendar.getInstance()
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY)
        selectedMinute = calendar.get(Calendar.MINUTE)
        updateTimeDisplay()  // Call function to display time

        // Set up select time button
        selectTimeButton.setOnClickListener {
            showTimePickerDialog()  // Call function to show time picker
        }

        // Set up alarm button
        setAlarmButton.setOnClickListener {
            setAlarm()  // Call function to set alarm
        }
    }

    // Function to show time picker dialog
    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                updateTimeDisplay()  // Update displayed time after selection
            },
            selectedHour,
            selectedMinute,
            true // Use 24-hour format
        )
        timePickerDialog.show()
    }

    // Function to update the time display
    private fun updateTimeDisplay() {
        val hourString = if (selectedHour < 10) "0$selectedHour" else selectedHour.toString()
        val minuteString = if (selectedMinute < 10) "0$selectedMinute" else selectedMinute.toString()
        timeDisplayTextView.text = "Selected Time: $hourString:$minuteString"
    }

    // Function to set the alarm
    private fun setAlarm() {
        val alarmMessage = alarmMessageEditText.text.toString().ifEmpty { "Wake up for class!" } // Default message

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, selectedHour)
            putExtra(AlarmClock.EXTRA_MINUTES, selectedMinute)
            putExtra(AlarmClock.EXTRA_MESSAGE, alarmMessage)
            putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        }

        try {
            startActivity(intent)
            Toast.makeText(this, "Alarm request sent", Toast.LENGTH_SHORT).show()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No alarm app available - would set alarm for $selectedHour:$selectedMinute", Toast.LENGTH_LONG).show()
            Log.d("AlarmApp", "Would have set alarm for $selectedHour:$selectedMinute with message: $alarmMessage")
        }
    }
}
