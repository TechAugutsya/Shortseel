package com.example.shortseel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shortseel.databinding.ActivityMainBinding
import com.example.shortseel.fragment.HomeFragment


class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            val homeFragment = HomeFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(binding.flFragment.id, homeFragment)
                commit()
            }
        }

    }
}