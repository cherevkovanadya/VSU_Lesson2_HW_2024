package com.example.vsu_lesson2_hw_2024

import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val surfBroadcastReceiver = SurfBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val resolver = contentResolver
        val uri = Uri.parse("content://dev.surf.android.provider/text")

        val cursor = resolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val text = it.getString(it.getColumnIndexOrThrow("text"))
                Toast.makeText(
                    applicationContext,
                    text,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val filter = IntentFilter("ru.shalkoff.vsu_lesson2_2024.SURF_ACTION")
        registerReceiver(surfBroadcastReceiver, filter, RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        unregisterReceiver(surfBroadcastReceiver)
        super.onDestroy()
    }

}