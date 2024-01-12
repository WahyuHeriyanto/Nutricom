package com.codevolt.nutricom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.content.Intent

class MainActivity2 : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work)

        val runningOption:ImageButton = findViewById(R.id.oneOption)
        runningOption.setOnClickListener(this)

        val cyclingOption:ImageButton = findViewById(R.id.twoOption)
        cyclingOption.setOnClickListener(this)

        val homeButton:ImageButton = findViewById(R.id.homeButton)
        homeButton.setOnClickListener(this)

        val shopButton:ImageButton = findViewById(R.id.trashButton)
        shopButton.setOnClickListener(this)

        val aboutButton:ImageButton = findViewById(R.id.peopleButton)
        aboutButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.oneOption -> {
                    val runIntent = Intent(this, MapsActivity::class.java)
                    startActivity(runIntent)
                }
                R.id.twoOption ->{
                    val cyclingIntent = Intent(this, MapsActivity::class.java)
                    startActivity(cyclingIntent)
                }

                R.id.homeButton -> {
                    val homeIntent = Intent(this, MainActivity::class.java)
                    startActivity(homeIntent)
                }

                R.id.trashButton -> {
                    val shoppingIntent = Intent(this, ShoppingActivity::class.java)
                    startActivity(shoppingIntent)
                }

                R.id.peopleButton -> {
                    val settingIntent = Intent(this, SettingsActivity::class.java)
                    startActivity(settingIntent)
                }

            }
        }
    }
}