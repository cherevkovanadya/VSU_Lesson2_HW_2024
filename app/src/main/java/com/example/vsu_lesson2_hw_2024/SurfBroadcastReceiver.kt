package com.example.vsu_lesson2_hw_2024

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class SurfBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "ru.shalkoff.vsu_lesson2_2024.SURF_ACTION") {
            val message = intent.getStringExtra("message")
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()
            Log.d("receiver", "$message")
        }
    }

}