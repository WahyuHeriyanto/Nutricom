package com.codevolt.nutricom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.codevolt.nutricom.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater())
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeButton -> replaceFragment(HomeFragment())
                R.id.itemActivity -> replaceFragment(ActivityFragment())
                R.id.itemTrash -> replaceFragment(ShopFragment())
                R.id.itemChannel -> replaceFragment(ChannelFragment())

                else ->{


                }

            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, fragment)
        fragmentTransaction.commit()
    }
}

