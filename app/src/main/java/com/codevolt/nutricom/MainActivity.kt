package com.codevolt.nutricom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.content.Intent

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val runningButton:ImageButton = findViewById(R.id.runButton)
        runningButton.setOnClickListener(this)

        val shoppingButton:ImageButton = findViewById(R.id.trashButton)
        shoppingButton.setOnClickListener(this)

        val settingButton:ImageButton = findViewById(R.id.peopleButton)
        settingButton.setOnClickListener(this)

        val notificationButton:ImageButton = findViewById(R.id.notificationButton)
        notificationButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.runButton -> {
                    val intent = Intent(this, MainActivity2::class.java)
                    startActivity(intent)
                }
                R.id.trashButton -> {
                    val shopIntent = Intent(this, ShoppingActivity::class.java)
                    startActivity(shopIntent)
                }
                R.id.peopleButton -> {
                    val aboutIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(aboutIntent)
                }
                R.id.notificationButton -> {
                    val notificationIntent = Intent(this, NotificationActivity::class.java)
                    startActivity(notificationIntent)
                }

            }
        }
    }
}