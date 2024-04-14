package com.example.vsu_lesson2_hw_2024

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vsu_lesson2_hw_2024.databinding.ActivityMainBinding

const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"

class MainActivity : AppCompatActivity() {
    companion object {
        const val SECRET_KEY = "SECRET_KEY"
        const val MESSAGE = "MESSAGE"
        const val EMPTY = ""
    }

    private var secretKey = EMPTY
    private var message = EMPTY

    private lateinit var binding: ActivityMainBinding
    private val surfBroadcastReceiver = SurfBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPostNotifications(this)
        binding.getSecretKeyButton.setOnClickListener {
            getSecretKey()
        }
        binding.getSavedData.setOnClickListener {
            getSavedData()
        }
        receiveMessages()
    }

    override fun onDestroy() {
        unregisterReceiver(surfBroadcastReceiver)
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SECRET_KEY, secretKey)
        message = getSharedPreferences()
        outState.putString(MESSAGE, message)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        secretKey = savedInstanceState.getString(SECRET_KEY, "")
        message = savedInstanceState.getString(MESSAGE, "")
    }

    private fun requestPostNotifications(context: Context) {
        val permissionStatus =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun getSecretKey() {
        val resolver = contentResolver
        val uri = Uri.parse("content://dev.surf.android.provider/text")

        val cursor = resolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                secretKey = it.getString(it.getColumnIndexOrThrow("text"))
                Toast.makeText(
                    applicationContext,
                    secretKey,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getSavedData() {
        if (secretKey != "" || message != "") {
            Toast.makeText(
                applicationContext,
                "Сохраненные данные: $secretKey $message",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(applicationContext, "Сохраненных данных нет", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun receiveMessages() {
        val filter = IntentFilter("ru.shalkoff.vsu_lesson2_2024.SURF_ACTION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(surfBroadcastReceiver, filter, RECEIVER_EXPORTED)
        }
    }

    private fun getSharedPreferences(): String {
        val sharedPreferences =
            applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(MESSAGE, "").toString()
    }
}